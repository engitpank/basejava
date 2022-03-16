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
    public void updateStorage(Resume r, Object index) {
        storage.set((int) index, r);
    }

    @Override
    public Resume getFromStorage(Object index) {
        return storage.get((int) index);
    }

    @Override
    public void deleteFromStorage(Object index) {
        storage.remove((int) index);
    }

    @Override
    public void saveToStorage(Resume r, Object index) {
        storage.add(r);
    }

    @Override
    protected boolean isExist(Object index) {
        return (int) index >= 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        return new ArrayList<>(storage);
    }

    @Override
    public int size() {
        return storage.size();
    }

    protected Integer findIndex(String uuid) {
        int index = 0;
        for (Resume resume : storage) {
            if (Objects.equals(resume.getUuid(), uuid)) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
