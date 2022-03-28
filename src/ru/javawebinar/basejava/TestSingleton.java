package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.SectionType;

import java.util.HashMap;
import java.util.Map;

public class TestSingleton {
    private static final Map<String, SectionType> sectionTypes = new HashMap<>();
    private static TestSingleton instance;

    private TestSingleton() {

    }

    public static TestSingleton getInstance() {
        if (instance == null) {
            instance = new TestSingleton();
        }
        return instance;
    }

    public static void main(String[] args) {
        TestSingleton.getInstance().toString();
        TestSingleton ts = new TestSingleton();
        Singleton instance = Singleton.INSTANCE;
        System.out.println(instance.name());
        System.out.println(instance.ordinal());
        for (SectionType type : SectionType.values()) {
            System.out.println(type.getTitle());
        }
    }

    public enum Singleton {
        INSTANCE
    }
}
