package ru.bsc.workingtoolbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bsc.workingtoolbot.main.impl.JsonParser;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final JsonParser jsonParser;
    @GetMapping("/test")
    public JsonNode get() {
        String test = "testField M boolean\n" +
            "testFiel1 M decimal(2,3)";
        JsonNode node = jsonParser.parse(test);
        return node;
    }
}
