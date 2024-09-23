package dn.rubtsov.parserj_02.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "mapping")
@Getter
@Setter
public class MappingConfiguration {
    private Map<String, String> message;
    private Map<String, String> header;
    private Map<String, String> dfaRegisterPosition;
    private Map<String, String> registers;

}

