package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    protected abstract void saveToStorage(Resume r, Object index);

    protected abstract void updateStorage(Resume r, Object index);

    protected abstract Resume getFromStorage(Object index);

    protected abstract void deleteFromStorage(Object index);

    protected abstract Object findIndex(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract List<Resume> getStorageForSort();

    @Override
    public void save(Resume r) {
        Object index = getSearchKeyIfNotExist(r.getUuid());
        saveToStorage(r, index);
    }

    @Override
    public void update(Resume r) {
        Object index = getSearchKeyIfExist(r.getUuid());
        updateStorage(r, index);
    }

    @Override
    public Resume get(String uuid) {
        Object index = getSearchKeyIfExist(uuid);
        return getFromStorage(index);
    }

    @Override
    public void delete(String uuid) {
        Object index = getSearchKeyIfExist(uuid);
        deleteFromStorage(index);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> allResume = getStorageForSort();
        allResume.sort(Comparator.nullsLast(
                Comparator.comparing(Resume::getFullName).
                        thenComparing(Comparator.naturalOrder())
        ));
        return allResume;
    }

    private Object getSearchKeyIfExist(String uuid) {
        Object index = findIndex(uuid);
        if (!isExist(index)) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private Object getSearchKeyIfNotExist(String uuid) {
        Object index = findIndex(uuid);
        if (isExist(index)) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }
}
