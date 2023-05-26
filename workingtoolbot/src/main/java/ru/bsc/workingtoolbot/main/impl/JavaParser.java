package ru.bsc.workingtoolbot.main.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.dto.ClassParameterDto;

@Component
public class JavaParser {


    public void createTestClasses() {

    }

    public String getContent(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(String.format("%s\n", line));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ClassParameterDto> getParametersFromClass() {
        return null;
    }
}
