package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStorage extends AbstractStorage {
    protected final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void updateResume(Resume r, int index) {
        storage.set(index, r);
    }

    @Override
    public Resume getFromStorage(int index) {
        return storage.get(index);
    }

    @Override
    public void deleteFromStorage(int index) {
        storage.remove(index);
    }

    @Override
    public void saveToStorage(Resume r, int index) {
        storage.add(r);
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        storage.toArray(resumes);
        return resumes;
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected int findIndex(String uuid) {
        for (Resume resume : storage) {
            if (Objects.equals(resume.getUuid(), uuid)) {
                return storage.indexOf(resume);
            }
        }
        return -1;
    }
}
