package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SQLStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config CONFIG = new Config();
    protected static final File PROP_DIR = new File("\\.storage\\resumes.properties");
    private final File storageDir;
    private final Storage storage;

    private Config() {
        try (InputStream is = new FileInputStream("./config/resume.properties")) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SQLStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db" +
                    ".password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROP_DIR.getAbsolutePath());
        }
    }

    public static Config get() {
        return CONFIG;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }
}
