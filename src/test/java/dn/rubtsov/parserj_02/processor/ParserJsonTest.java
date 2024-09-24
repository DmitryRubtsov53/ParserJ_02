package dn.rubtsov.parserj_02.processor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParserJsonTest {
    @BeforeAll
    static void setUp() {
        // Создание таблицы перед запуском тестов
        DBUtils.createTableIfNotExists("registers");
    }


    @Test
    void testSelectData() {
        DBUtils.insertRecords(ParserJson.parseJsonFiles());
        DBUtils.selectAllRecords().forEach(e -> System.out.println(e.getRegisterType() + " " + e.getRestIn()));
    }

}
