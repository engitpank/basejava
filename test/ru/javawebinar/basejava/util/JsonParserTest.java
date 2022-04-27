package ru.javawebinar.basejava.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.model.AbstractSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SimpleLineSection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javawebinar.basejava.ResumeTestData.RESUME_1;

class JsonParserTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void read() {
        String json = JsonParser.write(RESUME_1);
        Resume resume = JsonParser.read(json, Resume.class);
        assertEquals(RESUME_1, resume);
    }

    @Test
    void write() {
        AbstractSection expectedSection = new SimpleLineSection("ObjectiveOOO");
        String json = JsonParser.write(expectedSection, AbstractSection.class);
        AbstractSection actualSection = JsonParser.read(json, AbstractSection.class);
        assertEquals(expectedSection, actualSection);
    }
}