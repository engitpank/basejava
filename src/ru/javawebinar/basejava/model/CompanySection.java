package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class CompanySection {
    private final LinkSection site;
    private final List<Experience> experienceList;

    public CompanySection(String titleLink, String link, List<Experience> experienceList) {
        site = new LinkSection(titleLink, link);
        this.experienceList = experienceList;
    }

    public LinkSection getSite() {
        return site;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    @Override
    public String toString() {
        return site + " " + experienceList.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;

        if (!Objects.equals(site, that.site)) return false;
        return experienceList.equals(that.experienceList);
    }

    @Override
    public int hashCode() {
        int result = site.hashCode();
        result = 31 * result + experienceList.hashCode();
        return result;
    }
}
