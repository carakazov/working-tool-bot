package ru.bsc.workingtoolbot.utils.jsoncreator.impl;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.dto.JsonStringRecord;
import ru.bsc.workingtoolbot.generators.ValueGenerator;
import ru.bsc.workingtoolbot.utils.jsoncreator.JsonCreator;

@Component
@RequiredArgsConstructor
public class JsonCreatorImpl implements JsonCreator {
    private static final String STRING_TEMPLATE = "\"{FIELD}\":\"{VALUE}\"";
    private static final String FIELD_PLACEHOLDER = "{FIELD}";
    private static final String VALUE_PLACEHOLDER = "{VALUE}";

    private final Map<String, ValueGenerator> valueGenerators;

    @Override
    public JsonNode create(JsonCreationDto request)  {
        StringBuilder builder = new StringBuilder("{");
        request.strings().forEach(string -> builder.append(createString(string)));
        builder.append("}");
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(builder.toString());
        } catch(Exception e) {
            throw new RuntimeException("Json creation error");
            //TODO на моменте создания обработки ошибок что-нибудь придумать
        }
    }

    private String createString(JsonStringRecord stringRecord) {
        String bounds = stringRecord.bounds();
        ValueGenerator generator = valueGenerators.entrySet().stream()
            .filter(entry -> entry.getKey().contains(stringRecord.dataType()))
            .findFirst()
            .get()
            .getValue();
        //TODO проверка, если по данному типу не найдено, то идем в бд и достаем оттуда существуюший json
        String value = generator.generate(bounds, stringRecord.regex());
        return STRING_TEMPLATE
            .replace(FIELD_PLACEHOLDER, stringRecord.title())
            .replace(VALUE_PLACEHOLDER, value);
    }
}
