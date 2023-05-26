package ru.bsc.workingtoolbot.utils.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.JsonCreationDto;
import ru.bsc.workingtoolbot.dto.JsonStringRecord;
import ru.bsc.workingtoolbot.utils.mapper.JsonCreationMapper;

@Component
public class JsonCreationMapperImpl implements JsonCreationMapper {
    private static final Integer FIELD_TITLE_INDEX = 0;
    private static final Integer FIELD_REQUIRED_MARK_INDEX = 1;
    private static final Integer FIELD_DATA_TYPE_INDEX = 2;
    private static final Integer FIELD_REGEX_INDEX = 3;

    @Override
    public JsonCreationDto toDto(String request) {
        List<JsonStringRecord> jsonStrings = new ArrayList<>();
        for(String line : request.split("\n")) {
            String[] params = line.split(" ");
            String title = params[FIELD_TITLE_INDEX];
            String required = params[FIELD_REQUIRED_MARK_INDEX];
            String dataType = params[FIELD_DATA_TYPE_INDEX];
            String regEx = params.length == 4 ? params[FIELD_REGEX_INDEX] : "";
            jsonStrings.add(new JsonStringRecord(title, "M".equals(required), dataType, regEx));
        }
        return new JsonCreationDto(jsonStrings);
    }
}
