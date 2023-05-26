package ru.bsc.workingtoolbot.main.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.main.Parser;
import ru.bsc.workingtoolbot.model.TestDataTemplate;
import ru.bsc.workingtoolbot.service.TestDataTemplateService;
import ru.bsc.workingtoolbot.utils.jsoncreator.JsonCreator;
import ru.bsc.workingtoolbot.utils.mapper.JsonCreationMapper;
import ru.bsc.workingtoolbot.validation.Validator;

@Component
@RequiredArgsConstructor
public class JsonParser implements Parser {
    private final JsonCreationMapper jsonCreationMapper;
    private final JsonCreator jsonCreator;
    private final Validator validator;
    private final TestDataTemplateService testDataTemplateService;

    @Override
    public JsonNode parse(String request, Long chatId) {
        JsonCreationDto creationDto = jsonCreationMapper.toDto(request);
        List<TestDataTemplate> templates = testDataTemplateService.findAllByChatId(chatId);
        validator.validate(creationDto, templates.stream().map(TestDataTemplate::getName).collect(Collectors.toList()));
        return jsonCreator.create(creationDto, templates);
    }
}
