package ru.bsc.workingtoolbot.validation;

import java.util.List;

import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.dto.JsonStringRecord;

public interface Validator {
    void validate(JsonCreationDto creationDto, List<String> userDtos);
}
