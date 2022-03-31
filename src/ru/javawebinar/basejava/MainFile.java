package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainFile {

    static void showFiles(File directory, String indent) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(indent + "File: " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println(indent + "Directory: " + file.getName());
                    showFiles(file, indent + indent.charAt(0));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = "./.idea/.gitignore";
        File file = new File(filePath);
        System.out.println(file.getCanonicalPath());
        File dir = new File("./.idea");
        System.out.println(dir.isDirectory());
        System.out.println(Arrays.toString(dir.list()));

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(Arrays.toString(fis.readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String directoryPath = "./";
        showFiles(new File(directoryPath), " ");
    }
}
