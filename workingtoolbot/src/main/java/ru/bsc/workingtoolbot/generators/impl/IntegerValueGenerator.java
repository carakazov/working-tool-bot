package ru.bsc.workingtoolbot.generators.impl;

import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
        return changeFirstZero(RandomStringUtils.random(length, false, true));
    }

    @Override
    protected String generateWithoutBounds() {
        return changeFirstZero(RandomStringUtils.random(3, false, true));
    }

    @Override
    public String getType() {
        return "int";
    }
}
