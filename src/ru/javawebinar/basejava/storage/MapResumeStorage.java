package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    protected final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume r, Resume resume) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void updateStorage(Resume r, Resume resume) {
        storage.replace(resume.getUuid(), r);
    }

    @Override
    protected Resume getFromStorage(Resume resume) {
        return resume;
    }

    @Override
    protected void deleteFromStorage(Resume resume) {
        storage.remove(resume.getUuid());
    }

    @Override
    protected Resume findSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Resume resume) {
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
