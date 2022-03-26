package ru.javawebinar.basejava.model;

import java.util.Objects;

public class LinkSection {
    private final String link;
    private final String title;

    public LinkSection(String title, String link) {
        this.link = link;
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + "(" + link + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinkSection that = (LinkSection) o;

        if (!Objects.equals(link, that.link)) return false;
        return title.equals(that.title);
    }

    @Override
    public int hashCode() {
        int result = link != null ? link.hashCode() : 0;
        result = 31 * result + title.hashCode();
        return result;
    }
}
