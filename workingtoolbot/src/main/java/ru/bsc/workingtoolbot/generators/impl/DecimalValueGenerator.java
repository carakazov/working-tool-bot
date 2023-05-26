package ru.bsc.workingtoolbot.generators.impl;

import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.generators.ValueGenerator;

@Component
public class DecimalValueGenerator extends ValueGenerator {
    private static final Integer INTEGER_VALUE_INDEX = 0;
    private static final Integer DECIMAL_VALUE_INDEX = 1;
    private static final String DECIMAL_NUMBER_TEMPLATE = "{INTEGER},{DECIMAL}";
    private static final String INTEGER_PLACEHOLDER = "{INTEGER}";
    private static final String DECIMAL_PLACEHOLDER = "{DECIMAL}";

    @Override
    public String generate(String bounds, String regEx) {
        return super.generate(bounds, regEx);
    }

    @Override
    protected String generateWhenBounds(String bounds) {
        String[] numberBounds = bounds.split(",");
        int integerLength = Integer.parseInt(numberBounds[INTEGER_VALUE_INDEX]);
        int decimalLength = Integer.parseInt(numberBounds[DECIMAL_VALUE_INDEX]);
        String integer = RandomStringUtils.random(integerLength, false, true);
        String decimal = RandomStringUtils.random(decimalLength, false, true);

        return DECIMAL_NUMBER_TEMPLATE
            .replace(INTEGER_PLACEHOLDER, integer)
            .replace(DECIMAL_PLACEHOLDER, decimal);
    }

    @Override
    protected String generateWithoutBounds() {
        String integer = RandomStringUtils.random(3, false, true);
        String decimal = RandomStringUtils.random(2,  false, true);

        return DECIMAL_NUMBER_TEMPLATE
            .replace(INTEGER_PLACEHOLDER, integer)
            .replace(DECIMAL_PLACEHOLDER, decimal);
    }
}
