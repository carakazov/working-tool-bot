package ru.bsc.workingtoolbot.main.impl;

import java.util.*;
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

    @Override
    public TestClassesDto createClasses(List<ClassDto> classes) {
        List<String> constants = new ArrayList<>();
        List<String> functions = new ArrayList<>();
        Map<String, String> fabricFunctions = new HashMap<>();
        for(ClassDto classDto : classes) {
            boolean allFieldsSimple = true;
            Map<String, String> constantLines = new HashMap<>();
            for(ClassParameterDto parameter : classDto.getParameters()) {
                Optional<ConstantGenerator> generator = constantGenerators.stream()
                    .filter(item -> item.getTypes().contains(parameter.getType()))
                    .findFirst();
                if(generator.isPresent()) {
                    String constantLine = generator.get().constant(parameter, classDto.getClassName());
                    constants.add(constantLine);
                    constantLines.put(parameter.getName(), constantLine);
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
            } else {
                List<ClassParameterDto> nonFilledParameters = classDto.getParameters().stream()
                    .filter(item -> !item.getIsFilled())
                    .toList();
                nonFilledParameters.forEach(item -> {
                    String neededFunction = fabricFunctions.get(item.getType());
                    String oldFunction = fabricFunctions.get(classDto.getClassName());
                    StringBuilder builder = new StringBuilder();
                    builder.append(SET_LINE_TEMPLATE.replace(FIELD_PLACEHOLDER, item.getName())
                        .replace(VALUE_PLACEHOLDER, neededFunction));
                    builder.append("; \n}");
                    functions.add(builder.toString());
                    item.setIsFilled(true);
                });
                classDto.setProceeded(true);
            }
        }
        return null;
        //TODO вставка двух массиов в файлы
    }

    private String generateFabricFunction(String className, Map<String, String> constants) {
        String classInserted = FABRIC_FUNCTION_TEMPLATE.replaceAll(CLASS_PLACEHOLDER, className);
        StringBuilder builder = new StringBuilder(classInserted);
        constants.forEach((entry, key) -> {
            String constantName = StringUtils.substringBetween(key, " ", " =");
            String setLine = SET_LINE_TEMPLATE
                .replace(FIELD_PLACEHOLDER, entry)
                .replace(VALUE_PLACEHOLDER, constantName);
            builder.append(setLine);
        });
        return builder.toString();
    }

}
