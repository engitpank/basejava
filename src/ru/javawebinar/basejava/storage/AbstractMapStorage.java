package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public abstract class AbstractMapStorage extends AbstractStorage {
    protected final Map<String, Resume> storage = new HashMap<>();

    protected abstract String findIndexBy(String searchKey);

    @Override
    protected void saveToStorage(Resume r, Object uuid) {
        storage.put((String) uuid, r);
    }

    @Override
    protected void updateStorage(Resume r, Object uuid) {
        storage.replace((String) uuid, r);
    }

    @Override
    protected Resume getFromStorage(Object uuid) {
        return storage.get((String) uuid);
    }

    @Override
    protected void deleteFromStorage(Object uuid) {
        storage.remove((String) uuid);
    }

    @Override
    protected String findIndex(String searchKey) {
        return findIndexBy(searchKey);
    }

    @Override
    protected boolean isExist(Object uuid) {
        return storage.containsKey((String) uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    public List<Resume> getStorageForSort(){
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
