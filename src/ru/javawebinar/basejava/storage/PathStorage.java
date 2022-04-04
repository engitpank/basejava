package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final StreamSerializer streamSerializer;

    protected PathStorage(String dir, StreamSerializer streamSerializer) {
        Objects.requireNonNull(dir, "directory must not be null");
        Objects.requireNonNull(streamSerializer, "The serialization strategy must not be null");
        this.directory = Paths.get(dir);
        this.streamSerializer = streamSerializer;
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected void saveToStorage(Resume r, Path pathToResume) {
        try {
            Files.createFile(pathToResume);
        } catch (IOException e) {
            throw new StorageException(getFileName(pathToResume), "Couldn't create file " + pathToResume, e);
        }
        updateStorage(r, pathToResume);
    }

    @Override
    protected void updateStorage(Resume r, Path pathToResume) {
        try {
            streamSerializer.writeToFile(r, new BufferedOutputStream(Files.newOutputStream(pathToResume)));
        } catch (IOException e) {
            throw new StorageException(getFileName(pathToResume), "IO error " + pathToResume, e);
        }
    }

    @Override
    protected Resume getFromStorage(Path pathToResume) {
        try {
            return streamSerializer.readFile(new BufferedInputStream(Files.newInputStream(pathToResume)));
        } catch (IOException e) {
            throw new StorageException(getFileName(pathToResume), "IO error " + pathToResume, e);
        }
    }

    @Override
    protected void deleteFromStorage(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException("File couldn't be deleted", e);
        }
    }

    @Override
    protected Path findSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path file) {
        return Files.isRegularFile(file);
    }

    @Override
    protected List<Resume> getStorageAsList() {
        return getListFiles().map(this::getFromStorage).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getListFiles().forEach(this::deleteFromStorage);
    }

    @Override
    public int size() {
        return (int) getListFiles().count();
    }

    private Stream<Path> getListFiles() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory read error", e);
        }
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }
}
