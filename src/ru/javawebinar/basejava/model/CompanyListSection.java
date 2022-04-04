package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

public class CompanyListSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<CompanySection> companySections;

    public CompanyListSection() {
    }

    public CompanyListSection(CompanySection... companies) {
        this(Arrays.asList(companies));
    }

    public CompanyListSection(List<CompanySection> companySections) {
        this.companySections = companySections;
    }

    public List<CompanySection> getCompanySections() {
        return companySections;
    }

    @Override
    public String toString() {
        return companySections.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyListSection that = (CompanyListSection) o;

        return companySections.equals(that.companySections);
    }

    @Override
    public int hashCode() {
        return companySections.hashCode();
    }
}
