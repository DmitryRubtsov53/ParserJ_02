package dn.rubtsov.parserj_02.processor;

import dn.rubtsov.parserj_02.data.Registers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ParserJsonTest {
    @Autowired
    ParserJson parserJson;
    @Autowired
    JsonProducer jsonProducer;

    @BeforeAll
    static void setUp() {

        DBUtils.dropTableIfExists();

        DBUtils.createTableIfNotExists("registers");
    }

    @AfterAll
    static void tearDown(@Autowired JsonProducer jsonProducer) {

        if (jsonProducer != null) {
            jsonProducer.close();
        }
    }

    @Test
    void testOfParseJsonFilesAndSendToKafka() throws Exception {

        try (InputStream inputStream = getClass().getResourceAsStream("/Test.json")) {
            assertNotNull(inputStream, "InputStream should not be null");
            String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            jsonProducer.sendMessage(jsonContent);
            Thread.sleep(7000);
        }
    }

    @Test
    void testSelectData() {
        List<Registers> records = DBUtils.selectAllRecords();
        assertNotNull(records, "Records should not be null");
        System.out.println("Number of records: " + records.size());
        assertEquals(3,records.size());
        DBUtils.selectAllRecords().forEach(e -> System.out.println(e.getRegisterType()
                + " " + e.getRestIn()));
    }

}