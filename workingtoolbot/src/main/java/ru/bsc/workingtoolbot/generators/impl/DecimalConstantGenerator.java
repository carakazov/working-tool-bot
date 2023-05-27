package ru.bsc.workingtoolbot.generators.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;
import ru.bsc.workingtoolbot.generators.ConstantGenerator;

@Component
public class DecimalConstantGenerator extends ConstantGenerator {
    private static final List<String> TYPES = List.of("float", "Float", "double", "Double", "BigDecimal");

    @Override
    public String constant(ClassParameterDto classParameter, String className) {
        String type = classParameter.getType();
        String title = getTitle(className, classParameter.getName());
        if(type.equals("float") || type.equals("Float")) {
            return createConstantLine(type, title, "1F");
        } else if(type.equals("double") || type.equals("Double")) {
            return createConstantLine(type, title, "1.0");
        } else {
            return createConstantLine(type, title, "BigDecimal.ONE");
        }
    }

    @Override
    public List<String> getTypes() {
        return TYPES;
    }
}
