package ru.bsc.workingtoolbot.generators.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.generators.ValueGenerator;
import ru.bsc.workingtoolbot.utils.StringGenerator;

@Component
public class StringValueGenerator extends ValueGenerator {

    @Override
    public String generate(String bounds, String regEx) {
        return super.generate(bounds, regEx);
    }

    @Override
    protected String generateWhenBounds(String bounds) {
        int length = Integer.parseInt(bounds);
        return StringGenerator.generateString(length);
    }

    @Override
    protected String generateWithoutBounds() {
        return StringGenerator.generateString(8);
    }

    @Override
    public String getType() {
        return "string";
    }

}
