package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Map;

public class MapStorageByFullName extends AbstractMapStorage {
    @Override
    protected String findIndexBy(String searchKey) {
        for (Map.Entry<String, Resume> resumeEntry : storage.entrySet()) {
            if (resumeEntry.getValue().getFullName().equals(searchKey)) {
                return resumeEntry.getKey();
            }
        }
        return searchKey;
    }
}
