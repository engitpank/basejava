package ru.javawebinar.basejava.storage.serialize;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializeStrategy {
    Resume readFile(InputStream is) throws IOException;

    void writeToFile(Resume r, OutputStream os) throws IOException;
}
