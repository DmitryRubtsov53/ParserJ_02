package dn.rubtsov.parserj_02;

import dn.rubtsov.parserj_02.data.Registers;

import dn.rubtsov.parserj_02.processor.DBUtils;
import dn.rubtsov.parserj_02.processor.ParserJson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.util.List;
@SpringBootApplication
@EnableConfigurationProperties
public class ParserJ_02Application implements CommandLineRunner {

    private final ParserJson parserJson;

    @Autowired
    public ParserJ_02Application(ParserJson parserJson) {
        this.parserJson = parserJson;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParserJ_02Application.class, args);
    }
    @Override
    public void run(String... args) {

        List<Registers> data = parserJson.parseJsonFiles();
        // Запись данных в БД
        DBUtils.insertRecord(data);
        //Вывод в консоль этих же данных
        data.forEach(e -> System.out.println("registerType: " + e.getRegisterType()
        + ", " + "restIn: " + e.getRestIn()));

    }

}