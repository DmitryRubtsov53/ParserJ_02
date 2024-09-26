package dn.rubtsov.parserj_02.processor;

import dn.rubtsov.parserj_02.dto.MessageDB;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParserJsonTest {
    @Autowired
    ParserJson parserJson;

    @Autowired
    JsonProducer jsonProducer;

    @BeforeAll
    static void setUp() {

        DBUtils.dropTableIfExists("message_db");

        DBUtils.createTableIfNotExists("message_db");
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

            // Отправка сообщения в Kafka
            jsonProducer.sendMessage(jsonContent);
            Thread.sleep(7000);
        }
    }

    @Test
    void testSelectData() {
        List<MessageDB> messages = DBUtils.selectAllRecords();
        assertNotNull(messages, "Messages should not be null");
        System.out.println("\n Number of messages: " + messages.size());
        DBUtils.selectAllRecords().forEach(e -> System.out.println(
                e.getHeader().getProductId()
                        + " " + e.getHeader().getMessageId()
                        + " " + e.getHeader().getAccountingDate()
                        + " " + e.getRegisters().getRegisterType()
                        + " " + e.getRegisters().getRestIn()));
        System.out.println();
    }

}
