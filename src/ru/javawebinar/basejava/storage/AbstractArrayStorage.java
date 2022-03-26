package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.*;

abstract public class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    protected abstract Integer findSearchKey(String uuid);

    protected abstract void deleteFromArray(int index);

    protected abstract void saveToArray(Resume r, int index);

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void updateStorage(Resume r, Integer index) {
        storage[index] = r;
    }

    @Override
    public Resume getFromStorage(Integer index) {
        return storage[index];
    }

    @Override
    public void deleteFromStorage(Integer index) {
        deleteFromArray(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public void saveToStorage(Resume r, Integer index) {
        if (size >= storage.length) {
            throw new StorageException(r.getUuid(), "Storage overflow");
        }
        saveToArray(r, index);
        size++;
    }

    @Override
    public List<Resume> getStorageForSort() {
        return Arrays.asList(storage).subList(0, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }
}