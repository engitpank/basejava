package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract void saveToStorage(Resume r, SK searchKey);

    protected abstract void updateStorage(Resume r, SK searchKey);

    protected abstract Resume getFromStorage(SK searchKey);

    protected abstract void deleteFromStorage(SK searchKey);

    protected abstract SK findSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);

    protected abstract List<Resume> getStorageAsList();

    @Override
    public void save(Resume r) {
        LOG.info("Save " + r);
        SK searchKey = getSearchKeyIfNotExist(r.getUuid());
        saveToStorage(r, searchKey);
    }

    @Override
    public void update(Resume r) {
        LOG.info("Update " + r);
        SK searchKey = getSearchKeyIfExist(r.getUuid());
        updateStorage(r, searchKey);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SK searchKey = getSearchKeyIfExist(uuid);
        return getFromStorage(searchKey);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SK searchKey = getSearchKeyIfExist(uuid);
        deleteFromStorage(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> allResume = getStorageAsList();
        LOG.info("GetAllSorted " + allResume);
        allResume.sort(Comparator.nullsLast(
                Comparator.comparing(Resume::getFullName).
                        thenComparing(Resume::getUuid)
        ));
        return allResume;
    }

    private SK getSearchKeyIfExist(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getSearchKeyIfNotExist(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
