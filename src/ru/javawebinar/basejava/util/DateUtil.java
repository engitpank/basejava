package ru.javawebinar.basejava.util;

import java.time.YearMonth;

public class DateUtil {
    public static final YearMonth NOW = YearMonth.of(3000, 1);
    public static YearMonth of(int year, int month) {
        return YearMonth.of(year, month);
    }
}
