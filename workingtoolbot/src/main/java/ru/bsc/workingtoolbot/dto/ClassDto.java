package ru.bsc.workingtoolbot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassDto {
    private String className;
    private Boolean proceeded;
    private List<ClassParameterDto> parameters;
}
