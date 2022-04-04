package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume r, Resume resumeAsKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void updateStorage(Resume r, Resume resumeAsKey) {
        storage.replace(resumeAsKey.getUuid(), r);
    }

    @Override
    protected Resume getFromStorage(Resume resumeAsKey) {
        return resumeAsKey;
    }

    @Override
    protected void deleteFromStorage(Resume resumeAsKey) {
        storage.remove(resumeAsKey.getUuid());
    }

    @Override
    protected Resume findSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Resume resumeAsKey) {
        return resumeAsKey != null;
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
