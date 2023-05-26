package ru.bsc.workingtoolbot.generators.impl;

import java.util.Random;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.generators.ValueGenerator;

@Component
public class BooleanValueGenerator extends ValueGenerator {
    @Override
    public String generate(String bounds, String regEx) {
        return super.generate(bounds, regEx);
    }

    @Override
    protected String generateWhenBounds(String bounds) {
        return generateInternal();
    }

    @Override
    protected String generateWithoutBounds() {
        return generateInternal();
    }

    public String generateInternal() {
        int number = new Random().nextInt(0, 2);
        if(number == 0) {
            return "false";
        } else {
            return "true";
        }
    }
}
