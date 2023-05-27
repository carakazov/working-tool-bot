package ru.bsc.workingtoolbot.main.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassDto;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;
import ru.bsc.workingtoolbot.dto.TestClassesDto;
import ru.bsc.workingtoolbot.generators.ConstantGenerator;
import ru.bsc.workingtoolbot.main.TestClassCreator;

@Component
@RequiredArgsConstructor
public class TestClassCreatorImpl implements TestClassCreator {
    private final Set<ConstantGenerator> constantGenerators;
    private static final String FABRIC_FUNCTION_TEMPLATE = "public static TITLE getTITLE() { \n return new TITLE()";
    private static final String SET_LINE_TEMPLATE = ".setFIELD(VALUE)";
    private static final String CLASS_PLACEHOLDER = "TITLE";
    private static final String FIELD_PLACEHOLDER = "FIELD";
    private static final String VALUE_PLACEHOLDER = "VALUE";
    private static final String TEST_DATA_CONSTANTS_CLASS_TEMPLATE = "public class TestDataConstants { \n {CONTENT} \n }";
    private static final String FABRIC_FUNCTIONS_CLASS_TEMPLATE = "public class FabricFunctionUtils { \n {CONTENT} \n }";
    private static final String CONTENT_PLACEHOLDER = "{CONTENT}";

    @Override
    public TestClassesDto createClasses(List<ClassDto> classes) {
        List<String> constants = new ArrayList<>();
        List<String> functions = new ArrayList<>();
        Map<String, String> fabricFunctions = new HashMap<>();
        for(ClassDto classDto : classes) {
            boolean allFieldsSimple = true;
            Map<String, LineInfo> constantLines = new HashMap<>();
            for(ClassParameterDto parameter : classDto.getParameters()) {
                String fullType = parameter.getType();
                String type;
                boolean isList = false;
                if(fullType.contains("List") || fullType.contains("Set")) {
                    type = StringUtils.substringBetween(fullType, "<", ">");
                    isList = true;
                } else {
                    type = fullType;
                }
                Optional<ConstantGenerator> generator = constantGenerators.stream()
                    .filter(item -> item.getTypes().contains(type))
                    .findFirst();
                if(generator.isPresent()) {
                    String constantLine = generator.get().constant(parameter, classDto.getClassName());
                    constants.add(constantLine);
                    constantLines.put(parameter.getName(), new LineInfo(constantLine, isList));
                    parameter.setIsFilled(true);
                } else {
                    allFieldsSimple = false;
                }
            }
            String fabricFunction = generateFabricFunction(classDto.getClassName(), constantLines);
            fabricFunctions.put(classDto.getClassName(), fabricFunction);
            classDto.setProceeded(allFieldsSimple);
        }
        for(ClassDto classDto : classes) {
            if(Boolean.TRUE.equals(classDto.getProceeded())) {
                String fabricFunction = fabricFunctions.get(classDto.getClassName());
                StringBuilder builder = new StringBuilder(fabricFunction);
                builder.append("; \n}");
                functions.add(builder.toString());
                fabricFunctions.put(classDto.getClassName(), builder.toString());
            } else {
                List<ClassParameterDto> nonFilledParameters = classDto.getParameters().stream()
                    .filter(item -> !item.getIsFilled())
                    .toList();
                StringBuilder builder = new StringBuilder();
                nonFilledParameters.forEach(item -> {
                    builder.delete(0, builder.toString().length());
                    String type = item.getType();
                    String neededFunction;
                    boolean isList = false;
                    if(type.contains("List") || type.contains("Set")) {
                        neededFunction = fabricFunctions.get(StringUtils.substringBetween(type, "<", ">"));
                        isList = true;
                    } else {
                        neededFunction = fabricFunctions.get(item.getType());
                    }
                    String functionName = neededFunction.split(" ")[3];
                    String oldFunction = fabricFunctions.get(classDto.getClassName());
                    String value;
                    if(isList) {
                        value = "Collections.singletonList(" + functionName + ")";
                    } else {
                        value = functionName;
                    }
                    builder.append(oldFunction);
                    builder.append(SET_LINE_TEMPLATE.replace(FIELD_PLACEHOLDER, item.getName().substring(0, 1).toUpperCase(
                            Locale.ROOT) + item.getName().substring(1))
                        .replace(VALUE_PLACEHOLDER, value));
                    item.setIsFilled(true);
                    fabricFunctions.put(classDto.getClassName(), builder.toString());
                });
                if(classDto.getParameters().stream().allMatch(ClassParameterDto::getIsFilled)) {
                    builder.append("; \n}");
                    classDto.setProceeded(true);
                }
                functions.add(builder.toString());
                fabricFunctions.put(classDto.getClassName(), builder.toString());
            }
        }
        String concatConstants = concatList(constants);
        String concatFunctions = concatList(functions);
        return new TestClassesDto(
            TEST_DATA_CONSTANTS_CLASS_TEMPLATE.replace(CONTENT_PLACEHOLDER, concatConstants),
            FABRIC_FUNCTIONS_CLASS_TEMPLATE.replace(CONTENT_PLACEHOLDER, concatFunctions)
        );
    }


    private String concatList(List<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        strings.forEach(item -> stringBuilder.append(item).append("\n"));
        return stringBuilder.toString();
    }

    private String generateFabricFunction(String className, Map<String, LineInfo> constants) {
        String classInserted = FABRIC_FUNCTION_TEMPLATE.replaceAll(CLASS_PLACEHOLDER, className);
        StringBuilder builder = new StringBuilder(classInserted);
        constants.forEach((entry, key) -> {
            String[] parts = key.line.split(" ");
            String value = parts[4];
            if(Boolean.TRUE.equals(key.list)) {
                value = "Collections.singletonList(" + value + ")";
            }
            String setLine = SET_LINE_TEMPLATE
                .replace(FIELD_PLACEHOLDER, entry.substring(0, 1).toUpperCase(Locale.ROOT) + entry.substring(1))
                .replace(VALUE_PLACEHOLDER, value);
            builder.append(setLine);
        });
        return builder.toString();
    }

    private record LineInfo(String line, Boolean list) {

    }

}
