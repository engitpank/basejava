package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Link implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String link;
    private final String name;

    public Link(String name, String link) {
        Objects.requireNonNull(name, "title must not be null");
        this.link = link;
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "(" + link + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link that = (Link) o;

        if (!Objects.equals(link, that.link)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = link != null ? link.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }
}
