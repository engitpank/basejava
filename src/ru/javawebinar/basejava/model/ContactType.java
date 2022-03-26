package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Номер"),
    SKYPE("Skype"),
    MAIL("Почта"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    OWN_SITE("Домашняя страница");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
