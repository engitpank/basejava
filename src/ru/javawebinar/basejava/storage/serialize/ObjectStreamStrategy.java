package ru.javawebinar.basejava.storage.serialize;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamStrategy implements SerializeStrategy {
    @Override
    public Resume readFile(InputStream is) {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new StorageException(null, "Error read resume", e);
        }
    }

    @Override
    public void writeToFile(Resume r, OutputStream os) {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(r);
        } catch (IOException e) {
            throw new StorageException(r.getUuid(), "IO error", e);
        }
    }
}
