package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.AbstractSection;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.sql.SQLHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SQLStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SQLStorage.class.getName());
    private final SQLHelper sqlHelper;

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        sqlHelper = new SQLHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        LOG.info("Clear storage");
        sqlHelper.execute("TRUNCATE resume CASCADE");
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume.getUuid());
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                preparedStatement.setString(1, resume.getUuid());
                preparedStatement.setString(2, resume.getFullName());
                preparedStatement.execute();
            }
            insertContacts(resume, conn);
            insertSections(resume, conn);
            return null;
        });
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume.getUuid());
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE resume r SET  full_name=? WHERE r.uuid=?")) {
                preparedStatement.setString(1, resume.getFullName());
                preparedStatement.setString(2, resume.getUuid());
                if (preparedStatement.executeUpdate() != 1) {
                    throw new NotExistStorageException(resume.getUuid());
                }
                deleteContacts(resume, conn);
                deleteSections(resume, conn);
                insertContacts(resume, conn);
                insertSections(resume, conn);
                return null;
            }
        });
    }

    private void deleteSections(Resume resume, Connection conn) throws SQLException {
        deleteAttributes(resume, conn, "DELETE FROM section WHERE resume_uuid = ?");
    }

    private void deleteContacts(Resume resume, Connection conn) throws SQLException {
        deleteAttributes(resume, conn, "DELETE FROM contact WHERE resume_uuid = ?");
    }

    private void deleteAttributes(Resume resume, Connection conn, String sql) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, resume.getUuid());
            preparedStatement.execute();
        }
    }

    private void insertSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> section : resume.getSections().entrySet()) {
                preparedStatement.setString(1, resume.getUuid());
                preparedStatement.setString(2, section.getKey().name());
                String content = JsonParser.write(section.getValue(), AbstractSection.class);
                preparedStatement.setString(3, content);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private void insertContacts(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                preparedStatement.setString(1, resume.getUuid());
                preparedStatement.setString(2, contact.getKey().name());
                preparedStatement.setString(3, contact.getValue());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid =?")) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, resultSet.getString("full_name"));
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM contact c WHERE c.resume_uuid=?")) {
                preparedStatement.setString(1, uuid);
                ResultSet contactSet = preparedStatement.executeQuery();
                while (contactSet.next()) {
                    addContact(resume, contactSet);
                }
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM section s WHERE s.resume_uuid =?")) {
                preparedStatement.setString(1, uuid);
                ResultSet sectionSet = preparedStatement.executeQuery();
                while (sectionSet.next()) {
                    addSection(resume, sectionSet);
                }
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        sqlHelper.<Void>execute("DELETE FROM resume r WHERE r.uuid =?", (preparedStatement) -> {
            preparedStatement.setString(1, uuid);
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("Get all sorted storage");
        Map<String, Resume> resumes = new LinkedHashMap<>();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM resume r ORDER BY full_name,uuid")) {
                ResultSet resumeSet = preparedStatement.executeQuery();
                while (resumeSet.next()) {
                    String uuid = resumeSet.getString("uuid");
                    String fullName = resumeSet.getString("full_name");
                    resumes.put(uuid, new Resume(uuid, fullName));
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM contact c ORDER BY resume_uuid")) {
                ResultSet contactSet = preparedStatement.executeQuery();
                while (contactSet.next()) {
                    Resume resume = resumes.get(contactSet.getString("resume_uuid"));
                    addContact(resume, contactSet);
                }
            }
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM section s ORDER BY resume_uuid")) {
                ResultSet sectionSet = preparedStatement.executeQuery();
                while (sectionSet.next()) {
                    Resume resume = resumes.get(sectionSet.getString("resume_uuid"));
                    addSection(resume, sectionSet);
                }
            }
            return null;
        });
        return new ArrayList<>(resumes.values());
    }

    private void addSection(Resume resume, ResultSet sectionSet) throws SQLException {
        if (!sectionSet.wasNull()) {
            SectionType type = SectionType.valueOf(sectionSet.getString("type"));
            String content = sectionSet.getString("content");
            resume.setSection(type, JsonParser.read(content, AbstractSection.class));
        }
    }

    private void addContact(Resume resume, ResultSet contactSet) throws SQLException {
        if (!contactSet.wasNull()) {
            resume.setContact(ContactType.valueOf(contactSet.getString("type")), contactSet.getString("value"));
        }
    }

    @Override
    public int size() {
        LOG.info("Get storage size");
        return sqlHelper.execute("SELECT COUNT(*) AS count FROM resume r", (preparedStatement) -> {
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next() ? rs.getInt("count") : 0;
        });
    }
}
