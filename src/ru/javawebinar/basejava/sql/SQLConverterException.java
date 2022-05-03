package ru.javawebinar.basejava.sql;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.SQLException;

public class SQLConverterException {
    public static StorageException convert(SQLException e) {
        if (e instanceof PSQLException) {
            if (e.getSQLState().equals("23505")) return new ExistStorageException(e);
        }
        return new StorageException(e);
    }
}