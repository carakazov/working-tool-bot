package ru.bsc.workingtoolbot.generators.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.generators.ValueGenerator;

@Component
public class StringValueGenerator extends ValueGenerator {

    @Override
    public String generate(String bounds, String regEx) {
        return super.generate(bounds, regEx);
    }

    @Override
    protected String generateWhenBounds(String bounds) {
        int length = Integer.parseInt(bounds);
        return generate(length);
    }

    @Override
    protected String generateWithoutBounds() {
        return generate(8);
    }

    @Override
    public String getType() {
        return "string";
    }

    public String generate(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
