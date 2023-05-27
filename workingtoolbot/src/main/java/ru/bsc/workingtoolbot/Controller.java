package ru.bsc.workingtoolbot;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bsc.workingtoolbot.dto.ClassDto;
import ru.bsc.workingtoolbot.dto.TestClassesDto;
import ru.bsc.workingtoolbot.main.JavaReader;
import ru.bsc.workingtoolbot.main.TestClassCreator;
import ru.bsc.workingtoolbot.main.impl.JsonParser;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final JavaReader javaReader;
    private final TestClassCreator testClassCreator;
    @GetMapping("/test")
    public TestClassesDto get() {
        String address = "public class Address { \n private String address; \n }";
        String role = "public class Role { \n private String title; \n }";
        String user = "public class User { \n private String name; \n private List<Role> role; \n private Address address; \n}";;
        List<ClassDto> classes = javaReader.readClasses(List.of(user, role, address));
        TestClassesDto testClassesDto = testClassCreator.createClasses(classes);
        return testClassesDto;
    }
}
