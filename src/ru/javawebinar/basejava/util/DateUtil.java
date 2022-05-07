package ru.javawebinar.basejava.util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final YearMonth NOW = YearMonth.of(3000, 1);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final YearMonth TODAY = YearMonth.parse(YearMonth.now().format(FORMATTER));

    public static YearMonth of(int year, int month) {
        return YearMonth.of(year, month);
    }

    public static String format(YearMonth date) {
        if (date == null) {
            return "";
        } else if (date.equals(NOW)) {
            return "По настоящее время";
        }
        return date.format(FORMATTER);
    }

    public static YearMonth parse(String date) {
        if (date.equals("По настоящее время")) {
            return NOW;
        } else if (date.trim().length() == 0) {
            return TODAY;
        }
        return YearMonth.parse(date, FORMATTER);
    }
}
