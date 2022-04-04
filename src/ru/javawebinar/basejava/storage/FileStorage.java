package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final StreamSerializer streamSerializer;

    protected FileStorage(File directory, StreamSerializer streamSerializer) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(streamSerializer, "serialization strategy must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        this.directory = directory;
        this.streamSerializer = streamSerializer;
    }

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
            streamSerializer.writeToFile(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException(r.getUuid(), "IO error", e);
        }
    }

    @Override
    protected Resume getFromStorage(File file) {
        try {
            return streamSerializer.readFile(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException(file.getName(), "IO error", e);
        }
    }

    @Override
    protected void deleteFromStorage(File file) {
        if (!file.delete()) {
            throw new StorageException(file.getName(), "File delete error");
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
        File[] allResumeFiles = getListFiles();
        List<Resume> resumeList = new ArrayList<>();
        for (File file : allResumeFiles) {
            resumeList.add(getFromStorage(file));
        }
        return resumeList;
    }

    @Override
    public void clear() {
        File[] allResumes = getListFiles();
        for (File file : allResumes) {
            deleteFromStorage(file);
        }
    }

    @Override
    public int size() {
        return getListFiles().length;
    }

    private File[] getListFiles() {
        File[] list = directory.listFiles();
        if (list == null) {
            throw new StorageException("Directory read error");
        }
        return list;
    }
}
