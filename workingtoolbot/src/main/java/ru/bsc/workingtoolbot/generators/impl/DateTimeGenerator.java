package ru.bsc.workingtoolbot.generators.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;
import ru.bsc.workingtoolbot.generators.ConstantGenerator;

@Component
public class DateTimeGenerator extends ConstantGenerator {
    private static final List<String> TYPES = List.of("LocalDateTime", "LocalDate", "LocalTime");

    @Override
    public String constant(ClassParameterDto classParameter, String className) {
        String type = classParameter.getType();
        String title = getTitle(className, classParameter.getName());
        if(type.equals("LocalDateTime")) {
            return createConstantLine(type, title, "LocalDateTime.of(2023, 10, 10, 10, 10, 10)");
        } else if(type.equals("LocalDate")) {
            return createConstantLine(type, title, "LocalDate.of(2023, 10, 10)");
        } else {
            return createConstantLine(type, title, "LocalTime.of(10, 10, 10)");
        }
    }

    @Override
    public List<String> getTypes() {
        return TYPES;
    }
}
