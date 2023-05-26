package ru.bsc.workingtoolbot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public record JsonCreationDto(List<JsonStringRecord> strings) {
}
