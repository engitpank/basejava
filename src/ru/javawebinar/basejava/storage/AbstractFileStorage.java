package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        this.directory = directory;
    }

    protected abstract void writeToFile(Resume r, OutputStream os) throws IOException;

    protected abstract Resume readFile(InputStream is) throws IOException;

    @Override
    protected void saveToStorage(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException(r.getUuid(), "Couldn't create file: " + file.getAbsolutePath() + "\\" + file.getName(), e);
        }
        updateStorage(r, file);
    }

    @Override
    protected void updateStorage(Resume r, File file) {
        try {
            writeToFile(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException(r.getUuid(), "IO error: " + file.getName(), e);
        }
    }

    @Override
    protected Resume getFromStorage(File file) {
        try {
            return readFile(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException(file.getName(), "IO error: " + file.getName(), e);
        }
    }

    @Override
    protected void deleteFromStorage(File file) {
        if (!file.delete()) {
            throw new StorageException(file.getName(), "File delete error: " + file.getName());
        }
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> getStorageAsList() {
        File[] allResumeFiles = directory.listFiles();
        if (allResumeFiles == null) {
            throw new StorageException("Directory read error");
        }
        List<Resume> resumeList = new ArrayList<>();
        for (File file : allResumeFiles) {
            resumeList.add(getFromStorage(file));
        }
        return resumeList;
    }

    @Override
    public void clear() {
        File[] allResumes = directory.listFiles();
        if (allResumes != null) {
            for (File file : allResumes) {
                deleteFromStorage(file);
            }
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Directory read error");
        }
        return list.length;
    }
}
