package ru.javawebinar.basejava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.javawebinar.basejava.model.AbstractSection;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.YearMonth;

public class JsonParser {
    public static final Type YEAR_MONTH = new TypeToken<YearMonth>() {
    }.getType();
    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AbstractSection.class, new JsonSectionAdapter())
            .registerTypeAdapter(YEAR_MONTH, new JsonYearMonthAdapter())
            .setPrettyPrinting()
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }
}

