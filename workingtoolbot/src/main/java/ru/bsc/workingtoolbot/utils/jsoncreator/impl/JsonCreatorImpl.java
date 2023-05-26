package ru.bsc.workingtoolbot.utils.jsoncreator.impl;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.dto.JsonStringRecord;
import ru.bsc.workingtoolbot.generators.ValueGenerator;
import ru.bsc.workingtoolbot.model.TestDataTemplate;
import ru.bsc.workingtoolbot.utils.exception.LogicException;
import ru.bsc.workingtoolbot.utils.jsoncreator.JsonCreator;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonCreatorImpl implements JsonCreator {
    private static final String STRING_TEMPLATE = "\"{FIELD}\":{VALUE}";
    private static final String FIELD_PLACEHOLDER = "{FIELD}";
    private static final String VALUE_PLACEHOLDER = "{VALUE}";

    private final Set<ValueGenerator> generators;

    @Override
    public JsonNode create(JsonCreationDto request, List<TestDataTemplate> templates)  {
        StringBuilder builder = new StringBuilder("{");
        for(int i = 0; i < request.strings().size(); i++) {
            String value = createString(request.strings().get(i), templates);
            builder.append(value);
            if(i != request.strings().size() - 1) {
                builder.append(",");
            }
        }
        builder.append("}");
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(builder.toString());
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new LogicException("Произошла какая-то ошибка на сервере. Обратитесь к разработчику и попробуйте позже.");
        }
    }

    private String createString(JsonStringRecord stringRecord, List<TestDataTemplate> templates) {
        String bounds = stringRecord.bounds();
        Optional<ValueGenerator> generator = generators
            .stream()
            .filter(item -> stringRecord.dataType().contains(item.getType()))
            .findFirst();
        String value;
        if(generator.isPresent()) {
            value = generator.get().generate(bounds, stringRecord.regex());
            if(stringRecord.dataType().contains("string")) {
                value = "\"" + value + "\"";
            }
            return STRING_TEMPLATE
                .replace(FIELD_PLACEHOLDER, stringRecord.title())
                .replace(VALUE_PLACEHOLDER, value);
        }
        value = templates.stream()
            .filter(item -> item.getName().equals(stringRecord.dataType()))
            .findFirst()
            .get()
            .getTmp();
        return STRING_TEMPLATE
            .replace(FIELD_PLACEHOLDER, stringRecord.title())
            .replace(VALUE_PLACEHOLDER, value);
    }

}
