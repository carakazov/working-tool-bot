package ru.bsc.workingtoolbot;

import org.springframework.stereotype.Component;

@Component
public class MessageGenerator {
    public String generateStartMessage() {
        return "start";
    }

    public String generateCancelMessage() {
        return "Команда отменена";
    }

    public String generateHelpMessage() {
        return "help";
    }
}
