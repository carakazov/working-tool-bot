package ru.bsc.workingtoolbot.main;

import java.util.List;

import ru.bsc.workingtoolbot.dto.ClassDto;
import ru.bsc.workingtoolbot.dto.TestClassesDto;

public interface TestClassCreator {
    TestClassesDto createClasses(List<ClassDto> classes);
}
