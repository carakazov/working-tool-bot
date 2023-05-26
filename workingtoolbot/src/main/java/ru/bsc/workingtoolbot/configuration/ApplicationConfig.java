package ru.bsc.workingtoolbot.configuration;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bsc.workingtoolbot.generators.ValueGenerator;
import ru.bsc.workingtoolbot.generators.impl.BooleanValueGenerator;
import ru.bsc.workingtoolbot.generators.impl.DecimalValueGenerator;
import ru.bsc.workingtoolbot.generators.impl.IntegerValueGenerator;
import ru.bsc.workingtoolbot.generators.impl.StringValueGenerator;

@Configuration
public class ApplicationConfig {
    private static final String STRING_DATA_TYPE_MARKER = "string";
    private static final String INTEGER_DATA_TYPE_MARKER = "int";
    private static final String DECIMAL_DATA_TYPE_MARKER = "decimal";
    private static final String BOOLEAN_DATA_TYPE_MARKER = "boolean";

    @Bean
    public Map<String, ValueGenerator> valueGenerators(
        StringValueGenerator stringValueGenerator,
        IntegerValueGenerator integerValueGenerator,
        DecimalValueGenerator decimalValueGenerator,
        BooleanValueGenerator booleanValueGenerator
    ) {
        return Map.of(
            STRING_DATA_TYPE_MARKER, stringValueGenerator,
            INTEGER_DATA_TYPE_MARKER, integerValueGenerator,
            DECIMAL_DATA_TYPE_MARKER, decimalValueGenerator,
            BOOLEAN_DATA_TYPE_MARKER, booleanValueGenerator
        );
    }
}
