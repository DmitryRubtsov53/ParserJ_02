package dn.rubtsov.parserj_02;

import dn.rubtsov.parserj_02.processor.DBUtils;
import dn.rubtsov.parserj_02.processor.JsonProducer;
import dn.rubtsov.parserj_02.processor.ParserJson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@EnableConfigurationProperties
@SpringBootApplication
@EnableScheduling
public class ParserJ_02Application implements CommandLineRunner {

    @PostConstruct
    public void init(){
        DBUtils.createTableIfNotExists("message_db");
        try (InputStream inputStream = getClass().getResourceAsStream("/Test.json")) {
            String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            DBUtils.insertRecords(ParserJson.parseJson(jsonContent));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy(){
        DBUtils.dropTableIfExists("message_db");
    }
    private final ParserJson parserJson;
    @Autowired
    JsonProducer jsonProducer;
    @Autowired
    public ParserJ_02Application(ParserJson parserJson) {
        this.parserJson = parserJson;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParserJ_02Application.class, args);
    }
    @Override
    public void run(String... args) {

    }

}