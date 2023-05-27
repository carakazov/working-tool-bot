package ru.bsc.workingtoolbot.generators.impl;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Types;
import java.util.List;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;
import ru.bsc.workingtoolbot.generators.ConstantGenerator;

@Component
public class IntegerConstantGenerator extends ConstantGenerator {
    private static final List<String> TYPES = List.of("int", "Integer", "BigInteger", "long", "Long", "byte", "Byte");

    @Override
    public String constant(ClassParameterDto classParameter, String className) {
        String type = classParameter.getType();
        String title = getTitle(className, classParameter.getName());
        if(type.equals("int") || type.equals("Integer") || type.equals("byte") || type.equals("Byte")) {
            return createConstantLine(type, title, "1");
        } else if(type.equals("BigInteger")) {
            return createConstantLine(type, title, "BigInteger.ONE");
        } else {
            return createConstantLine(type, title, "1L");
        }
    }

    @Override
    public List<String> getTypes() {
        return TYPES;
    }
}
