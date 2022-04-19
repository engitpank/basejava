package ru.javawebinar.basejava.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super(uuid, "Resume " + uuid + " already exist");
    }

    public ExistStorageException(Exception e) {
        super("Resume already exist: ", e);
    }
}
