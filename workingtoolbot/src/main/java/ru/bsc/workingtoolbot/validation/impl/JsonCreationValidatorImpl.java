package ru.bsc.workingtoolbot.validation.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.dto.JsonStringRecord;
import ru.bsc.workingtoolbot.utils.exception.ValidationException;
import ru.bsc.workingtoolbot.validation.JsonCreationValidator;

@Component
public class JsonCreationValidatorImpl implements JsonCreationValidator {
    @Override
    public void validate(JsonCreationDto creationDto, List<String> userDtos) {
        List<String> errors = new ArrayList<>();
        for(JsonStringRecord stringRecord : creationDto.strings()) {
            String line = createLine(stringRecord);
            if(StringUtils.isBlank(stringRecord.dataType()) && StringUtils.isBlank(stringRecord.regex())) {
                errors.add("Не хватает обязательного поля - " + line);
            }
            if(!"M".equals(stringRecord.required()) && !"O".equals(stringRecord.required())) {
                errors.add("Признак обязательности должен быть либо 'M' либо 'O' - " + line);
            }
            if(StringUtils.isNotEmpty(stringRecord.regex()) && !stringRecord.regex().startsWith("\\")) {
                errors.add("Регулярное выражение должно начинаться с \\ - " + line);
            }
            if(isDataTypeIncorrect(stringRecord.dataType(), userDtos)) {
                errors.add("Неверный тип данных - " + line);
            }
        }

        if(!errors.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            errors.forEach(item -> builder.append(item).append("\n"));
            throw new ValidationException(builder.toString());
        }
    }

    private boolean isDataTypeIncorrect(String dataType, List<String> userDtos) {
        return !dataType.contains("string") &&
            !dataType.contains("int") &&
            !dataType.contains("decimal") &&
            !dataType.contains("boolean") &&
            !userDtos.contains(dataType);
    }

    private String createLine(JsonStringRecord record) {
        return record.title() + " " + record.required() + " " + record.dataType() + " " + record.regex();
    }
}
