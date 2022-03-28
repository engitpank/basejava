package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

abstract class AbstractArrayStorageTest extends AbstractStorageTest {


    AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    void storageOverflow() {
        try {
            for (int i = storage.size(); i < STORAGE_LIMIT; i++) {
                storage.save(new Resume("Biba"));
            }
        } catch (StorageException e) {
            fail("Переполнение хранилища произошло раньше, чем прошел тест");
        }
        assertThrows(StorageException.class, () -> storage.save(new Resume("Boba")));
    }

}