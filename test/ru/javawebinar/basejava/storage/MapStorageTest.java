package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class MapStorageTest extends AbstractStorageTest {

    MapStorageTest() {
        super(new MapStorage());
    }

    @Test
    void getAllSorted() {
        List<Resume> expected = Arrays.asList(new Resume(UUID_2), new Resume(UUID_1), new Resume(UUID_3));
        assertIterableEquals(expected, storage.getAllSorted());
    }

}