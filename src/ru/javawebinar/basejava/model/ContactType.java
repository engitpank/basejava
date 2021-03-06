package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Номер"),
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return "<a href='skype: " + value + ">" + value + "</a>";
        }
    },
    MAIL("Почта") {
        @Override
        public String toHtml0(String value) {
            return "<a href='mailto: " + value + ">" + value + "</a>";
        }
    },
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

    protected String toHtml0(String value){
        return title + ": " + value;
    }
    public String toHtml(String value) {
        return value == null ? "" : toHtml0(value);
    }

}
