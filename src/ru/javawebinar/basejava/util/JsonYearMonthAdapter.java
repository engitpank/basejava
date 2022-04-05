package ru.javawebinar.basejava.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class JsonYearMonthAdapter implements JsonSerializer<YearMonth>, JsonDeserializer<YearMonth> {
    @Override
    public YearMonth deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String ldtString = jsonElement.getAsString();
        return YearMonth.parse(ldtString, DateTimeFormatter.ofPattern("uuuu-MM"));
    }

    @Override
    public JsonElement serialize(YearMonth yearMonth, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(yearMonth.format(DateTimeFormatter.ofPattern("uuuu-MM")));
    }
}