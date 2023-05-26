package ru.bsc.workingtoolbot.utils.jsoncreator;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.model.TestDataTemplate;

public interface JsonCreator {
    JsonNode create(JsonCreationDto request, List<TestDataTemplate> templates);
}
