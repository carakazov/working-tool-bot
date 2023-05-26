package ru.bsc.workingtoolbot.main.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import ru.bsc.workingtoolbot.main.Parser;

@Component
public class JsonParser implements Parser {
    @Override
    public JsonNode parse(String request) {
        return null;
    }
}
