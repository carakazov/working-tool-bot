package ru.bsc.workingtoolbot.configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Set<ValueGenerator> generators(
        StringValueGenerator stringValueGenerator,
        IntegerValueGenerator integerValueGenerator,
        DecimalValueGenerator decimalValueGenerator,
        BooleanValueGenerator booleanValueGenerator
    ) {
        return Set.of(stringValueGenerator, integerValueGenerator, decimalValueGenerator, booleanValueGenerator);
    }
}
