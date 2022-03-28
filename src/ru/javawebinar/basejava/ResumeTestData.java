package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ResumeTestData {
    private static void printResume(Resume resume) {
        System.out.println(resume);
        System.out.println("==== Контакты  ====");
        for (ContactType contact : ContactType.values()) {
            System.out.println(contact + ": " + resume.getContact(contact));
        }
        for (SectionType section : SectionType.values()) {
            System.out.println("\n=== " + section + " ====");
            AbstractSection content = resume.getSection(section);
            System.out.println(content);
        }
    }

    public static Resume getTemplateFilledResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        // Contacts
        resume.addContact(ContactType.PHONE, "+7(921) 855-0482");
        resume.addContact(ContactType.SKYPE, "grigory.kislin");
        resume.addContact(ContactType.MAIL, "gkislin@yandex.ru");
        resume.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        resume.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.addContact(ContactType.OWN_SITE, "http://gkislin.ru/");

        // OBJECTIVE Section
        resume.addSection(SectionType.OBJECTIVE,
                new SimpleLineSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.addSection(SectionType.PERSONAL,
                new SimpleLineSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        // ACHIEVEMENTS Section
        List<String> achievements = new ArrayList<>();
        achievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. " +
                "Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн " +
                "стажировок и ведение проектов. Более 1000 выпускников.");
        achievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, " +
                "DuoSecurity, Google Authenticator, Jira, Zendesk.");
        resume.addSection(SectionType.ACHIEVEMENT, new BulletedListSection(achievements));

        // QUALIFICATIONS Section
        List<String> qualifications = new ArrayList<>();
        qualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2.");
        qualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce.");
        qualifications.add("Родной русский, английский \"upper intermediate\".");

        resume.addSection(SectionType.QUALIFICATIONS, new BulletedListSection(qualifications));

        // EXPERIENCE Section
        List<CompanySection> companies = new ArrayList<>();

        List<CompanySection.Experience> JProjectExperienceList = new ArrayList<>();
        JProjectExperienceList.add(new CompanySection.Experience(YearMonth.of(2013, 10), YearMonth.now(), "Автор проекта.",
                "Создание, организация и проведение Java онлайн проектов и стажировок."));
        companies.add(new CompanySection(new Link("Java Online Project", "http://javaops.ru/"), JProjectExperienceList));

        List<CompanySection.Experience> ritExperienceList = new ArrayList<>();

        ritExperienceList.add(new CompanySection.Experience(YearMonth.of(2012, 4), YearMonth.of(2014, 10),
                "Java архитектор",
                "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, " +
                        "ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO."
        ));
        companies.add(new CompanySection(new Link("RIT CENTER", null), ritExperienceList));
        List<CompanySection.Experience> WikeExperienceList = new ArrayList<>();

        WikeExperienceList.add(new CompanySection.Experience(YearMonth.of(2014, 10), YearMonth.of(2016, 1),
                "Старший разработчик (backend)",
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, " +
                        "MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, " +
                        "JWT SSO."));
        companies.add(new CompanySection(new Link("Wrike", "https://www.wrike.com/"), WikeExperienceList));

        List<CompanySection.Experience> LuxoftExperienceList = new ArrayList<>();
        LuxoftExperienceList.add(new CompanySection.Experience(YearMonth.of(2010, 12), YearMonth.of(2012, 4),
                "Ведущий программист",
                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle)." +
                        " Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и " +
                        "анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, " +
                        "Commet, HTML5."));
        companies.add(new CompanySection(new Link("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/"), LuxoftExperienceList));
        resume.addSection(SectionType.EXPERIENCE, new CompanyListSection(companies));

        // EDUCATION Section
        List<CompanySection> educationCompanies = new ArrayList<>();

        List<CompanySection.Experience> ITMOExperienceList = new ArrayList<>();
        ITMOExperienceList.add(new CompanySection.Experience(YearMonth.of(1993, 9), YearMonth.of(1996, 7), "Аспирантура (программист С, С++)", null));
        ITMOExperienceList.add(new CompanySection.Experience(YearMonth.of(1987, 9), YearMonth.of(1993, 6), "Инженер (программист Fortran, C)", null));
        educationCompanies.add(new CompanySection(new Link("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                "https://itmo.ru/ru/"), ITMOExperienceList));

        List<CompanySection.Experience> MFTIExperienceList = new ArrayList<>();
        MFTIExperienceList.add(new CompanySection.Experience(YearMonth.of(1984, 9), YearMonth.of(1987, 6), "Закончил с отличием", null));
        educationCompanies.add(new CompanySection(new Link("Заочная физико-техническая школа при МФТИ", "http://www.school.mipt.ru/"), MFTIExperienceList));

        resume.addSection(SectionType.EDUCATION, new CompanyListSection(educationCompanies));

        return resume;
    }

    public static void main(String[] args) {
        Resume resume = getTemplateFilledResume("Григорий Кислин", "2343");
        printResume(resume);
    }
}
