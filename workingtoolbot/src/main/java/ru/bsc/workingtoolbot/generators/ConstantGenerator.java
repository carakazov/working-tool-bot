package ru.bsc.workingtoolbot.generators;

import java.util.List;
import java.util.Locale;

import ru.bsc.workingtoolbot.dto.ClassParameterDto;

public abstract class  ConstantGenerator {
    private static final String CONSTANT_TEMPLATE = "public static final {TYPE} {TITLE} = {VALUE};";
    private static final String TYPE_PLACEHOLDER = "{TYPE}";
    private static final String TITLE_PLACEHOLDER = "{TITLE}";
    private static final String VALUE_PLACEHOLDER = "{VALUE}";

    public abstract String constant(ClassParameterDto classParameter, String className);

    public abstract List<String> getTypes();

    protected String createConstantLine(String type, String title, String value) {
        return CONSTANT_TEMPLATE
            .replace(TYPE_PLACEHOLDER, type)
            .replace(TITLE_PLACEHOLDER, title)
            .replace(VALUE_PLACEHOLDER, value);
    }

    protected String toScreamingSnakeCase(String string) {
        return string.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(
            Locale.ROOT);
    }
}
