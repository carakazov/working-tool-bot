package ru.bsc.workingtoolbot.utils.mapper;

import ru.bsc.workingtoolbot.dto.JsonCreationDto;

public interface JsonCreationMapper {
    JsonCreationDto toDto(String request);
}
