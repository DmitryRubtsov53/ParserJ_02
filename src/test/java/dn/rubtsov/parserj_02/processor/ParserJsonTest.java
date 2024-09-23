package dn.rubtsov.parserj_02.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import dn.rubtsov.parserj_02.data.Registers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ParserJsonTest {
@Autowired
ParserJson parserJson;

    @Test
    void testOfParseJsonFiles() {
        List<Registers> data = parserJson.parseJsonFiles();
        // Проверяем, что данные были корректно считаны
        assertNotNull(data, "List should not be null");
        assertFalse(data.isEmpty(), "List should not be empty");
        assertEquals(3, data.size(),"List's size should be 3");

        // Проверяем маппинг полей 1-ого элемента списка
        assertEquals("TEST3", data.get(0).getRegisterType(), "registerType should be 'TEST3'");
        assertEquals(1000000, data.get(0).getRestIn(), "restIn should be 1000000");
    }

    @Test
    void testParseJsonFiles_NegativeScenario_InvalidJsonFormat() throws IOException {
        // Arrange
        File resourcesDirectory = Paths.get("src", "test", "resources").toFile();
        File invalidJsonFile = new File(resourcesDirectory, "Test_invalid.json");
        try (FileWriter writer = new FileWriter(invalidJsonFile)) {
            writer.write("This is not a valid JSON format.");
        }

        // Act and Assert
        assertThrows(Exception.class, () -> parserJson.parseJsonFiles());
    }
}