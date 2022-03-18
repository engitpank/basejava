package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    protected abstract void saveToStorage(Resume r, Object searchKey);

    protected abstract void updateStorage(Resume r, Object searchKey);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract void deleteFromStorage(Object searchKey);

    protected abstract Object findSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract List<Resume> getStorageForSort();

    @Override
    public void save(Resume r) {
        Object searchKey = getSearchKeyIfNotExist(r.getUuid());
        saveToStorage(r, searchKey);
    }

    @Override
    public void update(Resume r) {
        Object searchKey = getSearchKeyIfExist(r.getUuid());
        updateStorage(r, searchKey);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getSearchKeyIfExist(uuid);
        return getFromStorage(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getSearchKeyIfExist(uuid);
        deleteFromStorage(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> allResume = getStorageForSort();
        allResume.sort(Comparator.nullsLast(
                Comparator.comparing(Resume::getFullName).
                        thenComparing(Resume::getUuid)
        ));
        return allResume;
    }

    private Object getSearchKeyIfExist(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getSearchKeyIfNotExist(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
