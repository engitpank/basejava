package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected abstract void saveToStorage(Resume r, int index);

    protected abstract void updateStorage(Resume r, int index);

    protected abstract Resume getFromStorage(int index);

    protected abstract void deleteFromStorage(int index);

    protected abstract int findIndex(String uuid);

    @Override
    public void save(Resume r) {
        int index = getNotExistElement(r.getUuid());
        saveToStorage(r, index);
    }

    @Override
    public void update(Resume r) {
        int index = getAlreadyExistElement(r.getUuid());
        updateStorage(r, index);
    }

    @Override
    public Resume get(String uuid) {
        int index = getAlreadyExistElement(uuid);
        return getFromStorage(index);
    }

    @Override
    public void delete(String uuid) {
        int index = getAlreadyExistElement(uuid);
        deleteFromStorage(index);
    }

    private int getAlreadyExistElement(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private int getNotExistElement(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }
}
