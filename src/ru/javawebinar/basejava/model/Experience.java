package ru.javawebinar.basejava.model;

import java.time.YearMonth;
import java.util.Objects;

public class Experience {
    private final YearMonth startDate;
    private final YearMonth finishDate;
    private final String title;
    private final String description;

    public Experience(YearMonth startDate, YearMonth finishDate, String title, String description) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.title = title;
        this.description = description;
    }

    public YearMonth getStartDate() {
        return startDate;
    }

    public YearMonth getFinishDate() {
        return finishDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return startDate + " - " + finishDate + " " + title + " " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experience that = (Experience) o;

        if (!startDate.equals(that.startDate)) return false;
        if (!finishDate.equals(that.finishDate)) return false;
        if (!title.equals(that.title)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + finishDate.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
