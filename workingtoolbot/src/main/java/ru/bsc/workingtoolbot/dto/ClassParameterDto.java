package ru.bsc.workingtoolbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassParameterDto {
    private String name;
    private String type;
    private Boolean isFilled;
}
