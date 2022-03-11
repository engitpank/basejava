package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected abstract void saveToStorage(Resume r, int index);
    protected abstract void updateResume(Resume r, int index);
    protected abstract Resume getFromStorage(int index);
    protected abstract void deleteFromStorage(int index);
    protected abstract int findIndex(String uuid);

    @Override
    public void save(Resume r) {
        int index = findIndex(r.getUuid());
        checkForExistStorageException(index, r.getUuid());
        saveToStorage(r, index);
    }

    @Override
    public void update(Resume r) {
        int index = findIndex(r.getUuid());
        checkForNotExistStorageException(index, r.getUuid());
        updateResume(r, index);
    }

    @Override
    public Resume get(String uuid) {
        int index = findIndex(uuid);
        checkForNotExistStorageException(index, uuid);
        return getFromStorage(index);
    }

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        checkForNotExistStorageException(index, uuid);
        deleteFromStorage(index);
    }

    private void checkForNotExistStorageException(int indexOfItemInStorage, String ErrorMessage) {
        if (indexOfItemInStorage < 0) {
            throw new NotExistStorageException(ErrorMessage);
        }
    }

    private void checkForExistStorageException(int indexOfItemInStorage, String ErrorMessage) {
        if (indexOfItemInStorage >= 0) {
            throw new ExistStorageException(ErrorMessage);
        }
    }
}
