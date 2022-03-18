package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer findSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid,"");
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void deleteFromArray(int index) {
        if (size - index >= 0) System.arraycopy(storage, index + 1, storage, index, size - index);
    }

    @Override
    protected void saveToArray(Resume r, int index) {
        index = -(index) - 1;
        if (size - index >= 0) System.arraycopy(storage, index, storage, index + 1, size - index);
        storage[index] = r;
    }
}
