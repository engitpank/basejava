package ru.javawebinar.basejava.model;

public class SimpleLineSection extends AbstractSection {
    private final String text;

    public SimpleLineSection(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleLineSection that = (SimpleLineSection) o;

        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}