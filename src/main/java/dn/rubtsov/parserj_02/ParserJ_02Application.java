package dn.rubtsov.parserj_02;

import dn.rubtsov.parserj_02.processor.ParserJson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

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
    }

}