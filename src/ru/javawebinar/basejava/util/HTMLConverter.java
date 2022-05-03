package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.*;

import java.util.List;

public class HTMLConverter {
    public static String convertContactToHTML(ContactType type, String value) {
        return value == null ? "" : switch (type) {
            case MAIL -> "<a href='mailto: " + value + ">" + value + "</a>";
            case PHONE -> "<a href='skype: " + value + ">" + value + "</a>";
            default -> type.getTitle() + ": " + value;
        };
    }

    public static String convertSectionToHTML(SectionType type, AbstractSection section) {
        return section == null ? "" : switch (type) {

            case PERSONAL, OBJECTIVE -> ((SimpleLineSection) section).getText();
            case ACHIEVEMENT, QUALIFICATIONS -> String.join("\n", ((BulletedListSection) section).getItems());
            case EXPERIENCE, EDUCATION -> {
                String resultHTML = "<div class='companies'>";
                List<CompanySection> companies = ((CompanyListSection) section).getCompanySections();
                for (CompanySection company : companies) {
                    resultHTML += "<div class='company>";
                    Link homePage = company.getHomePage();
                    homePage.getName();
                    homePage.getLink();
                    List<CompanySection.Experience> experiences = company.getExperienceList();
                    for (CompanySection.Experience experience : experiences) {
                        experience.getTitle();
                        experience.getDescription();
                        experience.getStartDate();
                        experience.getFinishDate();
                    }
                    resultHTML += "</div>";
                }
                resultHTML += "</div>";
                yield resultHTML;
            }
        };
    }

}
