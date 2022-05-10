package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.ContactType;

public class HTMLConverter {
    public static String convertContactToHTML(ContactType type, String value) {
        return value == null ? "" : switch (type) {
            case MAIL -> type.getTitle() + ": <a href='mailto: " + value + "'>" + value + "</a>";
            case PHONE -> type.getTitle() + ": <a href='tel:" + value + "'>" + value + "</a>";
            case SKYPE -> type.getTitle() + ": <a href='skype:" + value + "?call'>" + value + "</a>";
            default -> "<a href='" + value + "'>" + type.getTitle() + "</a>";
        };
    }

    public static int parseIntParameter(String value) {
        return value == null ? -1 : Integer.parseInt(value);
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
