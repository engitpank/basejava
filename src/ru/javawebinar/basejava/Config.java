package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SQLStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config CONFIG = new Config();
    protected static final String PROP_DIR = "/resume.properties";
    private final File storageDir;
    private final Storage storage;

    private Config() {
        try (InputStream is = Config.class.getResourceAsStream(PROP_DIR)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SQLStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db" +
                    ".password"));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Invalid config file " + PROP_DIR);
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

//    private static File getHomeDir() {
//        String prop = System.getProperty("homeDir");
//        File homeDir = new File(prop == null ? "." : prop);
//        if (!homeDir.isDirectory()) {
//            throw new IllegalStateException("homeDir is not directory");
//        }
//        return homeDir;
//    }

}
