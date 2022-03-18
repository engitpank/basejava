package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractStorageTest {

    protected final Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String FULLNAME_1 = "Tim";
    protected static final String FULLNAME_2 = "Mike";
    protected static final String FULLNAME_3 = "Kate";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1, FULLNAME_1));
        storage.save(new Resume(UUID_2, FULLNAME_2));
        storage.save(new Resume(UUID_3, FULLNAME_3));
    }

    @Test
    void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    void update() {
        Resume expected = new Resume(UUID_1, "Peppa");
        storage.update(expected);
        assertSame(expected, storage.get(UUID_1));
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("dummy")));
    }

    @Test
    void get() {
        assertEquals(new Resume(UUID_1, FULLNAME_1), storage.get(UUID_1));
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    void save() {
        Resume newR = new Resume("uuid_4", "Jack");
        storage.save(newR);
        assertEquals(newR, storage.get("uuid_4"));
        assertEquals(4, storage.size());
    }

    @Test
    void saveAlreadyExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(new Resume(UUID_1, FULLNAME_1)));
    }

    @Test
    void getAllSorted() {
        List<Resume> expected = Arrays.asList(new Resume(UUID_3, FULLNAME_3), new Resume(UUID_2, FULLNAME_2), new Resume(UUID_1, FULLNAME_1));
        List<Resume> actual = storage.getAllSorted();
        assertIterableEquals(expected, actual);
    }

    @Test
    void size() {
        assertEquals(3, storage.size());
    }
}