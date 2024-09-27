package dn.rubtsov.parserj_02.processor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dn.rubtsov.parserj_02.config.MappingConfiguration;
import dn.rubtsov.parserj_02.data.Header;
import dn.rubtsov.parserj_02.dto.MessageDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static dn.rubtsov.parserj_02.processor.DBUtils.getAndUpdateFirstRecordWithDispatchStatus;

@Component
public class ParserJson {
    private final MappingConfiguration mappingConfiguration;
    @Autowired
    JsonProducer jsonProducer;
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

    @Scheduled(cron = "0/10 * * * * ?")
    public void MessageDBToJson() {
        try {

            MessageDB messageDB = getAndUpdateFirstRecordWithDispatchStatus();
            if (messageDB == null) {
                System.out.println("Нет данных для обработки.");
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "resources", "test2.json").toFile();

            JsonNode jsonTemplate = objectMapper.readTree(jsonFile);
            // Рекурсивно мапим объект MessageDB на JSON-шаблон
            mapFieldsToJson(messageDB, jsonTemplate);
            // Преобразуем итоговый объект JsonNode обратно в строку
            String jsonString = objectMapper.writeValueAsString(jsonTemplate);
            System.out.println(jsonString);
            jsonProducer.sendMessage(jsonString);

        } catch (IOException | IllegalAccessException e) {
            System.err.println("Ошибка при обработке JSON или данных из базы.");
            e.printStackTrace();
        }
    }

    /** Метод для рекурсивного маппинга полей объекта MessageDB на JSON-шаблон
     *
     * @param sourceObject
     * @param jsonNode
     * @throws IllegalAccessException
     */
    private static void mapFieldsToJson(Object sourceObject, JsonNode jsonNode) throws IllegalAccessException {
        Field[] fields = sourceObject.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);  // Открываем доступ к private полям
            Object value = field.get(sourceObject);

            if (value != null) {

                if (!isPrimitiveOrWrapper(value.getClass())) {
                    mapFieldsToJson(value, jsonNode);
                } else {
                    // Если значение не объект, проверяем его в JSON
                    replaceValueInJson(jsonNode, field.getName(), value);
                }
            }
        }
    }

    /** Метод для замены значения в JSON с учетом массивов
     *
     * @param jsonNode
     * @param fieldName
     * @param value
     */
    private static void replaceValueInJson(JsonNode jsonNode, String fieldName, Object value) {
        if (jsonNode.has(fieldName)) {
            // Заменяем значение, если ключ найден
            ((ObjectNode) jsonNode).put(fieldName, value.toString());
        } else {
            // Проходим по дочерним узлам и проверяем массивы
            for (JsonNode childNode : jsonNode) {
                if (childNode.isObject()) {
                    replaceValueInJson(childNode, fieldName, value);
                } else if (childNode.isArray()) {
                    // Если узел является массивом, проходим по каждому элементу массива
                    for (JsonNode arrayItem : childNode) {
                        if (arrayItem.isObject()) {
                            replaceValueInJson(arrayItem, fieldName, value);
                        }
                    }
                }
            }
        }
    }


    /** Метод проверки, является ли класс примитивом или его оберткой
     *
     * @param type
     * @return
     */
    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                type == Integer.class || type == Long.class || type == Short.class || type == Byte.class ||
                type == Float.class || type == Double.class || type == Boolean.class || type == Character.class;
    }


}
