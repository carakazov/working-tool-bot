package ru.bsc.workingtoolbot.main.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.main.Parser;
import ru.bsc.workingtoolbot.model.TestDataTemplate;
import ru.bsc.workingtoolbot.service.TestDataTemplateService;
import ru.bsc.workingtoolbot.utils.jsoncreator.JsonCreator;
import ru.bsc.workingtoolbot.utils.mapper.JsonCreationMapper;
import ru.bsc.workingtoolbot.validation.JsonCreationValidator;

@Component
@RequiredArgsConstructor
public class JsonParser implements Parser {
    private final JsonCreationMapper jsonCreationMapper;
    private final JsonCreator jsonCreator;
    private final JsonCreationValidator jsonCreationValidator;
    private final TestDataTemplateService testDataTemplateService;

    @Override
    public JsonNode parse(String request, Long chatId) {
        JsonCreationDto creationDto = jsonCreationMapper.toDto(request);
        List<TestDataTemplate> templates = testDataTemplateService.findAllByChatId(chatId);
        jsonCreationValidator.validate(creationDto, templates.stream().map(TestDataTemplate::getName).collect(Collectors.toList()));
        return jsonCreator.create(creationDto, templates);
    }
}
