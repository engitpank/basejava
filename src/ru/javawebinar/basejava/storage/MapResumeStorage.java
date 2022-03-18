package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    protected final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume r, Object resume) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void updateStorage(Resume r, Object resume) {
        storage.replace(((Resume) resume).getUuid(), r);
    }

    @Override
    protected Resume getFromStorage(Object resume) {
        return (Resume) resume;
    }

    @Override
    protected void deleteFromStorage(Object resume) {
        storage.remove(((Resume) resume).getUuid());
    }

    @Override
    protected Resume findSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Object resume) {
        return resume != null;
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
