package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.*;

class MapStorageByFullNameTest extends AbstractMapStorageTest {

    MapStorageByFullNameTest() {
        super(new MapStorageByFullName());
    }

    @Test
    void update() {
        Resume expected = new Resume(UUID_1, "Tim");
        storage.update(expected);
        assertSame(expected, storage.get(FULLNAME_1));
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("dummy")));
    }

    @Test
    void get() {
        assertEquals(new Resume(UUID_1), storage.get(FULLNAME_1));
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    void delete() {
        storage.delete(FULLNAME_1);
        assertEquals(2, storage.size());
        assertThrows(NotExistStorageException.class, () -> storage.get(FULLNAME_1));
    }

    @Test
    void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    void save() {
        Resume newR = new Resume("uuid_4", "John");
        storage.save(newR);
        assertEquals(newR, storage.get("John"));
        assertEquals(4, storage.size());
    }

    @Test
    void saveAlreadyExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(new Resume(UUID_1)));
    }
}