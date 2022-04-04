package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamSerializer {
    Resume readFile(InputStream is) throws IOException;

    void writeToFile(Resume r, OutputStream os) throws IOException;
}
