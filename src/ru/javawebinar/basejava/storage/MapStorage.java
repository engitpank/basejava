package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

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
    protected String findIndex(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object uuid) {
        return storage.containsKey((String) uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
