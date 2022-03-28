package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUUIDStorage extends AbstractStorage<String> {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume r, String uuid) {
        storage.put(uuid, r);
    }

    @Override
    protected void updateStorage(Resume r, String uuid) {
        storage.replace(uuid, r);
    }

    @Override
    protected Resume getFromStorage(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void deleteFromStorage(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected String findSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String uuid) {
        return storage.containsKey(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    public List<Resume> getStorageAsList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
