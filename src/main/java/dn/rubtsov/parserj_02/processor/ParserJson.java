package dn.rubtsov.parserj_02.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import dn.rubtsov.parserj_02.config.MappingConfiguration;
import dn.rubtsov.parserj_02.data.Registers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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

    public List<Registers> parseJsonFiles() {
        List<Registers> data = new ArrayList<>();
        File resourcesDirectory = Paths.get("src", "main", "resources").toFile();
        ObjectMapper objectMapper = new ObjectMapper();

        for (File file : resourcesDirectory.listFiles((dir, name) -> name.endsWith(".json"))) {
            try {
                Map<String, Object> jsonMap = objectMapper.readValue(file, Map.class);

                if (jsonMap.containsKey("dfaRegisterPosition")) {
                    Map<String, Object> dfaRegisterPositionMap = (Map<String, Object>) jsonMap.get("dfaRegisterPosition");
                    if (dfaRegisterPositionMap.containsKey("registers")){
                        List<Map<String,Object>> registers = (List<Map<String,Object>>) dfaRegisterPositionMap.get("registers");
                        for (Map<String,Object> register : registers){
                            data.add(objectMapper.convertValue(register, dn.rubtsov.parserj_02.data.Registers.class));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
