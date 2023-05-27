package ru.bsc.workingtoolbot.main;

import com.fasterxml.jackson.databind.JsonNode;

public interface Parser {
    JsonNode parse(String request, Long chatId);
}
