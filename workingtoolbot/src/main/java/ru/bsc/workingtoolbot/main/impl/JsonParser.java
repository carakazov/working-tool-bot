package ru.bsc.workingtoolbot.main.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.main.Parser;
import ru.bsc.workingtoolbot.utils.jsoncreator.JsonCreator;
import ru.bsc.workingtoolbot.utils.mapper.JsonCreationMapper;

@Component
@RequiredArgsConstructor
public class JsonParser implements Parser {
    private final JsonCreationMapper jsonCreationMapper;
    private final JsonCreator jsonCreator;

    @Override
    public JsonNode parse(String request) {
        JsonCreationDto creationDto = jsonCreationMapper.toDto(request);
        return jsonCreator.create(creationDto);
    }
}
