package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

abstract public class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    protected abstract int findIndex(String uuid);

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = findIndex(r.getUuid());
        if (index != -1) {
            storage[index] = r;
        } else {
            System.out.println(r.getUuid() + ": отсутствует в storage");
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.println(uuid + ": отсутствует в storage");
        return null;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] allResume = new Resume[size];
        System.arraycopy(storage, 0, allResume, 0, allResume.length);
        return allResume;
    }

    public int size() {
        return size;
    }


}