package ru.bsc.workingtoolbot.utils.jsoncreator.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final Set<ValueGenerator> generators;

    @Override
    public JsonNode create(JsonCreationDto request)  {
        StringBuilder builder = new StringBuilder("{");
        for(int i = 0; i < request.strings().size(); i++) {
            builder.append(createString(request.strings().get(i)));
            if(i != request.strings().size() - 1) {
                builder.append(",");
            }
        }
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
        ValueGenerator generator = generators
            .stream()
            .filter(item -> stringRecord.dataType().contains(item.getType()))
            .findFirst()
            .get();
        //TODO проверка, если по данному типу не найдено, то идем в бд и достаем оттуда существуюший json
        String value = generator.generate(bounds, stringRecord.regex());
        return STRING_TEMPLATE
            .replace(FIELD_PLACEHOLDER, stringRecord.title())
            .replace(VALUE_PLACEHOLDER, value);
    }
}
