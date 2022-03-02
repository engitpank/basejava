package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

abstract public class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    protected abstract int findIndex(String uuid);
    protected abstract void deleteFromArray(int index);
    protected abstract void saveToArray(Resume r, int index);

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = findIndex(r.getUuid());
        if (index >= 0) {
            storage[index] = r;
        } else {
            System.out.println(r.getUuid() + ": отсутствует в storage");
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println(uuid + ": отсутствует в storage");
        return null;
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            deleteFromArray(index);
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Элемент отсутствие в storage");
        }

    }

    public void save(Resume r) {
        int index = findIndex(r.getUuid());
        if (size >= storage.length) {
            System.out.println("Добавление " + r.getUuid() + ". Переполнен storage");
        } else if (index >= 0) {
            System.out.println(r.getUuid() + ": Элемент уже добавлен");
        } else {
            saveToArray(r, index);
            size++;
        }
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