package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BulletedListSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> items;

    public BulletedListSection(String... items) {
        this(Arrays.asList(items));
    }

    public BulletedListSection(List<String> items) {
        Objects.requireNonNull(items, "companies must not be null");
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return items.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BulletedListSection that = (BulletedListSection) o;

        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
