package ru.bsc.workingtoolbot.generators.impl;

import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.generators.ValueGenerator;

@Component
public class IntegerValueGenerator extends ValueGenerator {
    @Override
    public String generate(String bounds, String regEx) {
        return super.generate(bounds, regEx);
    }

    @Override
    protected String generateWhenBounds(String bounds) {
        int length = Integer.parseInt(bounds);
        return RandomStringUtils.random(length, false, true);
    }

    @Override
    protected String generateWithoutBounds() {
        return RandomStringUtils.random(3, false, true);
    }

    @Override
    public String getType() {
        return "int";
    }
}
