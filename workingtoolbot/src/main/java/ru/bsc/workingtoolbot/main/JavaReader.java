package ru.bsc.workingtoolbot.main;

import java.util.List;

import org.checkerframework.checker.units.qual.C;
import ru.bsc.workingtoolbot.dto.ClassDto;

public interface JavaReader {
    List<ClassDto> readClasses(List<String> javaClasses);
}
