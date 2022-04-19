package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SQLHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SQLStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SQLStorage.class.getName());
    private final SQLHelper sqlHelper;

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) {
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
        sqlHelper.execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps ->
        {
            ps.setString(1, resume.getUuid());
            ps.setString(2, resume.getFullName());
            ps.execute();
            return null;
        });
        insertContacts(resume);
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume.getUuid());
        sqlHelper.<Void>execute("UPDATE resume r SET  full_name=? FROM resume WHERE r.uuid=?", preparedStatement -> {
            preparedStatement.setString(1, resume.getFullName());
            preparedStatement.setString(2, resume.getUuid());
            if (preparedStatement.executeUpdate() != 1) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return null;
        });
        deleteContacts(resume);
        insertContacts(resume);
    }

    private void deleteContacts(Resume resume) {
        sqlHelper.<Void>execute("DELETE FROM contact WHERE resume_uuid = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, resume.getUuid());
                    preparedStatement.execute();
                    return null;
                });
    }

    private void insertContacts(Resume resume) {
        sqlHelper.<Void>execute("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)", preparedStatement -> {
            for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                preparedStatement.setString(1, resume.getUuid());
                preparedStatement.setString(2, contact.getKey().name());
                preparedStatement.setString(3, contact.getValue());
                preparedStatement.execute();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return sqlHelper.execute(
                "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "     ON r.uuid = c.resume_uuid " +
                        "   WHERE r.uuid =?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet rs = preparedStatement.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    if (rs.getString("type") != null) {
                        do {
                            String value = rs.getString("value");
                            ContactType type = ContactType.valueOf(rs.getString("type"));
                            r.addContact(type, value);
                        }
                        while (rs.next());
                    }
                    return r;
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
        Map<String, Resume> resumeMap = new LinkedHashMap<>();
        sqlHelper.<Void>execute("SELECT * FROM resume r ORDER BY full_name,uuid", (preparedStatement) -> {
            ResultSet resumeSet = preparedStatement.executeQuery();
            while (resumeSet.next()) {
                String uuid = resumeSet.getString("uuid");
                String fullName = resumeSet.getString("full_name");
                resumeMap.put(uuid, new Resume(uuid, fullName));
            }
            return null;
        });
        sqlHelper.execute("SELECT * FROM contact c ORDER BY resume_uuid", (preparedStatement -> {
            ResultSet contactSet = preparedStatement.executeQuery();
            while (contactSet.next()) {
                Resume resume = resumeMap.get(contactSet.getString("resume_uuid"));
                resume.addContact(ContactType.valueOf(contactSet.getString("type")), contactSet.getString("value"));
            }
            return resumeMap;
        }));
        return new ArrayList<>(resumeMap.values());
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
