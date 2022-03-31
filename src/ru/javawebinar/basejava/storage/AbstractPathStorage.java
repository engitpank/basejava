package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;

    protected AbstractPathStorage(String dir) {
        Objects.requireNonNull(dir, "directory must not be null");

        this.directory = Paths.get(dir);

        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    protected abstract void writeToFile(Resume r, OutputStream os) throws IOException;

    protected abstract Resume readFile(InputStream is) throws IOException;

    @Override
    protected void saveToStorage(Resume r, Path pathToResume) {
        try {
            Files.createFile(pathToResume);
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + pathToResume.toAbsolutePath(), pathToResume.getFileName().toString(), e);
        }
        updateStorage(r, pathToResume);
    }

    @Override
    protected void updateStorage(Resume r, Path pathToResume) {
        try {
            writeToFile(r, new BufferedOutputStream(new FileOutputStream(pathToResume.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", pathToResume.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume getFromStorage(Path pathToResume) {
        try {
            return readFile(new BufferedInputStream(new FileInputStream(pathToResume.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", pathToResume.getFileName().toString(), e);
        }
    }

    @Override
    protected void deleteFromStorage(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException(null, "File couldn't be deleted");
        }
    }

    @Override
    protected Path findSearchKey(String uuid) {
        return Path.of(directory.toString(), uuid);
    }

    @Override
    protected boolean isExist(Path file) {
        return Files.exists(file);
    }

    @Override
    protected List<Resume> getStorageAsList() {
        List<Resume> resumeList = new ArrayList<>();
        try {
            Files.list(directory).forEach(path -> resumeList.add(getFromStorage(path)));
            return resumeList;
        } catch (IOException e) {
            throw new StorageException("Directory read error", null);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::deleteFromStorage);
        } catch (IOException e) {
            throw new StorageException(null, "Path delete error");
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException(null, "Unable to access the number of the directory");
        }
    }
}
