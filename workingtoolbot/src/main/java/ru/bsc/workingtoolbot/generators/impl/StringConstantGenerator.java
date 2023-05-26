package ru.bsc.workingtoolbot.generators.impl;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;
import ru.bsc.workingtoolbot.generators.ConstantGenerator;
import ru.bsc.workingtoolbot.utils.StringGenerator;

@Component
public class StringConstantGenerator extends ConstantGenerator {
    private static final List<String> TYPES = List.of("String", "char", "Char");

    @Override
    public String constant(ClassParameterDto classParameter, String className) {
        if("String".equals(classParameter.getType())) {
            return generateString(8, classParameter, className);
        } else {
            return generateString(1, classParameter, className);
        }
    }

    @Override
    public List<String> getTypes() {
        return TYPES;
    }

    private String generateString(int length, ClassParameterDto classParameter, String className) {
        String value = StringGenerator.generateString(8);
        String title = className + classParameter.getName().substring(0, 1).toUpperCase(Locale.ROOT) + classParameter.getName().substring(1);
        String snakeTitle = toScreamingSnakeCase(title);
        return createConstantLine(classParameter.getType(), snakeTitle, value);
    }

}
