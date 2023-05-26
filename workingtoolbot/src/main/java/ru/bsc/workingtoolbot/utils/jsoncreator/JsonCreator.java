package ru.bsc.workingtoolbot.utils.jsoncreator;

import com.fasterxml.jackson.databind.JsonNode;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;

public interface JsonCreator {
    JsonNode create(JsonCreationDto request);
}
