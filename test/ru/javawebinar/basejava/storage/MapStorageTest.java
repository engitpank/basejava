package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MapStorageTest extends AbstractStorageTest {

    MapStorageTest() {
        super(new MapStorage());
    }

    @Test
    void getAll() {
        Resume[] expected = {new Resume(UUID_2), new Resume(UUID_1), new Resume(UUID_3)};
        assertArrayEquals(expected, storage.getAll());
    }

}