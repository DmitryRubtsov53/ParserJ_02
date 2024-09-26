package dn.rubtsov.parserj_02.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import dn.rubtsov.parserj_02.config.MappingConfiguration;
import dn.rubtsov.parserj_02.data.Header;
import dn.rubtsov.parserj_02.dto.MessageDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ParserJson {
    private final MappingConfiguration mappingConfiguration;

    @Autowired
    public ParserJson(MappingConfiguration mappingConfiguration) {
        this.mappingConfiguration = mappingConfiguration;
    }

    public static List<MessageDB> parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<MessageDB> data = new ArrayList<>();
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
            Map<String, Object> headerMap = (Map<String, Object>) jsonMap.get("header");
            Header header = objectMapper.convertValue(headerMap, dn.rubtsov.parserj_02.data.Header.class);

            Map<String, Object> dfaRegisterPositionMap = (Map<String, Object>) jsonMap.get("dfaRegisterPosition");
            List<Map<String,Object>> registers = (List<Map<String,Object>>) dfaRegisterPositionMap.get("registers");
            for (Map<String,Object> register : registers){
                MessageDB messageDB = new MessageDB(header,
                        objectMapper.convertValue(register, dn.rubtsov.parserj_02.data.Registers.class));
                data.add(messageDB);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
