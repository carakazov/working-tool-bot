package ru.bsc.workingtoolbot.validation;

import java.util.List;

import ru.bsc.workingtoolbot.dto.JsonCreationDto;

public interface JsonCreationValidator {
    void validate(JsonCreationDto creationDto, List<String> userDtos);
}
