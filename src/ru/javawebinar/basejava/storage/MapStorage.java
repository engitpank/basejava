package ru.javawebinar.basejava.storage;

public class MapStorage extends AbstractMapStorage {

    @Override
    protected String findIndexBy(String searchKey) {
        return searchKey;
    }
}
