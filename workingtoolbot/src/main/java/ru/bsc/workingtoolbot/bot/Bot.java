package ru.bsc.workingtoolbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;

@Component
public class Bot extends AbilityBot {
    @Autowired
    public Bot(@Value("${application.bot.token}") String botToken, @Value("${application.bot.username}") String botUsername) {
        super(botToken, botUsername);
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public long creatorId() {
        return 0;
    }
}
