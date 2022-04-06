package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void writeToFile(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            writeCollection(dos, contacts.entrySet(), (entry) -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            Map<SectionType, AbstractSection> sections = r.getSections();
            writeCollection(dos, sections.entrySet(), (entry) -> {
                SectionType sectionType = entry.getKey();
                AbstractSection section = entry.getValue();
                dos.writeUTF(sectionType.name());
                writeSection(dos, sectionType, section);
            });
        }
    }

    @Override
    public Resume readFile(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readItems(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readItems(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> {
                return new SimpleLineSection(dis.readUTF());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                return new BulletedListSection(readList(dis, dis::readUTF));
            }
            case EXPERIENCE, EDUCATION -> {
                List<CompanySection> companySectionList = readList(dis, () -> {
                    String name = dis.readUTF();
                    String link = readUTFWithNullHandle(dis);
                    Link homePage = new Link(name, link);
                    List<CompanySection.Experience> experienceList = readList(dis, () -> {
                        YearMonth startDate = readYearMonth(dis);
                        YearMonth finishDate = readYearMonth(dis);
                        String title = dis.readUTF();
                        String description = readUTFWithNullHandle(dis);
                        return new CompanySection.Experience(startDate, finishDate, title, description);
                    });
                    return new CompanySection(homePage, experienceList);
                });
                return new CompanyListSection(companySectionList);
            }
            default -> throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, AbstractSection section) throws IOException {
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> dos.writeUTF(((SimpleLineSection) section).getText());
            case ACHIEVEMENT, QUALIFICATIONS -> {
                List<String> bulletedList = ((BulletedListSection) section).getItems();
                writeCollection(dos, bulletedList, dos::writeUTF);
            }
            case EXPERIENCE, EDUCATION -> {
                List<CompanySection> companyList = ((CompanyListSection) section).getCompanySections();
                writeCollection(dos, companyList, (companySection) -> {
                    Link homePage = companySection.getHomePage();
                    dos.writeUTF(homePage.getName());
                    writeUTFWithNullHandle(dos, homePage.getLink());
                    List<CompanySection.Experience> experienceList = companySection.getExperienceList();
                    writeCollection(dos, experienceList, (experience) -> {
                        writeYearMonth(dos, experience.getStartDate());
                        writeYearMonth(dos, experience.getFinishDate());
                        dos.writeUTF(experience.getTitle());
                        writeUTFWithNullHandle(dos, experience.getDescription());
                    });
                });
            }
            default -> throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }

    private void writeUTFWithNullHandle(DataOutputStream dos, String content) throws IOException {
        dos.writeUTF(content == null ? "" : content);
    }

    private String readUTFWithNullHandle(DataInputStream dis) throws IOException {
        String content = dis.readUTF();
        return content.equals("") ? null : content;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    private void writeYearMonth(DataOutputStream dos, YearMonth ym) throws IOException {
        dos.writeInt(ym.getYear());
        dos.writeInt(ym.getMonth().getValue());
    }

    private YearMonth readYearMonth(DataInputStream dis) throws IOException {
        return YearMonth.of(dis.readInt(), dis.readInt());
    }

    private void readItems(DataInputStream dis, ItemReader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private <T> List<T> readList(DataInputStream dis, ListReader<T> reader) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(reader.read());
        }
        return list;
    }

    private interface Writer<T> {
        void write(T t) throws IOException;
    }

    private interface ListReader<T> {
        T read() throws IOException;
    }

    private interface ItemReader {
        void read() throws IOException;
    }
}
