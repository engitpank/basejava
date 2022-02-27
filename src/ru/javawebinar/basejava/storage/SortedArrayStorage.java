package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {


    @Override
    public void save(Resume r) {
        int index = findIndex(r.getUuid());

        if (size >= storage.length) {
            System.out.println("Добавление " + r.getUuid() + ". Переполнен storage");
        } else if (index >= 0 && storage[index].equals(r)) {
            System.out.println(r.getUuid() + " Уже сохранён в storage");
        } else {
            if (index < 0) {
                index = -(index) - 1;
            }
            if (size - index >= 0) System.arraycopy(storage, index, storage, index + 1, size - index);
            storage[index] = r;
            size++;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            System.out.println(uuid + " для удаления не обнаружен. ");
        } else if (storage[index].getUuid().equals(uuid)) {
            size--;
            if (size - index >= 0) System.arraycopy(storage, index + 1, storage, index, size - index);
        }
    }

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
