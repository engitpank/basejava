package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.YearMonthAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.basejava.util.DateUtil.NOW;
import static ru.javawebinar.basejava.util.DateUtil.of;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanySection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Link homePage;
    private List<Experience> experienceList;

    public CompanySection() {
    }

    public CompanySection(String nameLink, String link, Experience... experiences) {
        this(new Link(nameLink, link), Arrays.asList(experiences));
    }

    public CompanySection(Link homePage, List<Experience> experienceList) {
        Objects.requireNonNull(experienceList, "experienceList must not be null");
        this.homePage = homePage;
        this.experienceList = experienceList;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    @Override
    public String toString() {
        return homePage + " " + experienceList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;

        if (!Objects.equals(homePage, that.homePage)) return false;
        return experienceList.equals(that.experienceList);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + experienceList.hashCode();
        return result;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Experience implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth startDate;
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth finishDate;
        private String title;
        private String description;

        public Experience() {
        }

        public Experience(int startMonth, int startYear, String title, String description) {
            this(of(startMonth, startYear), NOW, title, description);
        }

        public Experience(int startMonth, int startYear, int endMonth, int endYear, String title, String description) {
            this(of(startMonth, startYear), of(endYear, endMonth), title, description);
        }

        public Experience(YearMonth startDate, YearMonth finishDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(finishDate, "finishDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
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
}
