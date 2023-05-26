package ru.bsc.workingtoolbot.main.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassDto;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;
import ru.bsc.workingtoolbot.main.JavaReader;

@Component
public class JavaReaderImpl implements JavaReader {
    private static final Integer DATA_TYPE_INDEX = 1;
    private static final Integer FIELD_NAME_INDEX = 2;

    @Override
    public List<ClassDto> readClasses(List<String> javaClasses) {
        List<ClassDto> classes = new ArrayList<>();
        for(String javaClass : javaClasses) {
            String className = defineClassName(javaClass);
            String[] classLines = javaClass.split("\n");
            List<String> properties = Arrays.stream(classLines).filter(item -> item.contains("private")).map(String::trim).toList();
            List<ClassParameterDto> classParameters = new ArrayList<>();
            for(String property : properties) {
                String[] params = property.split(" ");
                String name = params[FIELD_NAME_INDEX];
                classParameters.add(new ClassParameterDto(name.substring(0, name.length() - 1), params[DATA_TYPE_INDEX], false));
            }
            classes.add(new ClassDto(className, false, classParameters));
        }
        return classes;
    }

    private String defineClassName(String javaClass) {
        return StringUtils.substringBetween(javaClass, "class", "{").trim();
    }

    public static void main(String[] args) {
        JavaReader javaReader = new JavaReaderImpl();
        String javaClass = "public class MyClass { \n private String title; \n private int length; \n}";
        String anotherClass = "public class AnotherClass { \n private String name; \n }";
        List<ClassDto> classes = javaReader.readClasses(List.of(javaClass, anotherClass));
        int a = 5;
    }
}
