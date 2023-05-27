package ru.bsc.workingtoolbot.generators.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;
import ru.bsc.workingtoolbot.generators.ConstantGenerator;

@Component
public class BooleanConstantGenerator extends ConstantGenerator {
    private static final List<String> TYPES = List.of("boolean", "Boolean");

    @Override
    public String constant(ClassParameterDto classParameter, String className) {
        String type = classParameter.getType();
        String title = getTitle(className, classParameter.getName());
        if(type.equals("boolean")) {
            return createConstantLine(type, title, "true");
        } else {
            return createConstantLine(type, title, "Boolean.TRUE");
        }
    }

    @Override
    public List<String> getTypes() {
        return TYPES;
    }
}
