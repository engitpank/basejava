package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void updateStorage(Resume r, Integer index) {
        storage.set(index, r);
    }

    @Override
    public Resume getFromStorage(Integer index) {
        return storage.get(index);
    }

    @Override
    public void deleteFromStorage(Integer index) {
        storage.remove(index.intValue());
    }

    @Override
    public void saveToStorage(Resume r, Integer index) {
        storage.add(r);
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    @Override
    protected List<Resume> getStorageAsList() {
        return new ArrayList<>(storage);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Integer findSearchKey(String uuid) {
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
