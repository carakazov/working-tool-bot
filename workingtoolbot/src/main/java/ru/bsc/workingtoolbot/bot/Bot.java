package ru.bsc.workingtoolbot.bot;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bsc.workingtoolbot.MessageGenerator;
import ru.bsc.workingtoolbot.main.Parser;
import ru.bsc.workingtoolbot.model.BotState;
import ru.bsc.workingtoolbot.service.ChatConfigService;
import ru.bsc.workingtoolbot.service.TestDataTemplateService;


@Component
public class Bot extends TelegramLongPollingBot {
    private final ChatConfigService chatConfigService;
    private final TestDataTemplateService testDataTemplateService;
    private final MessageGenerator messageGenerator;
    private final Parser parser;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    @Value("${application.bot.username}")
    private String botUsername;

    @Autowired
    public Bot(
        @Value("${application.bot.token}") String botToken, ChatConfigService chatConfigService,
        TestDataTemplateService testDataTemplateService, MessageGenerator messageGenerator,
        Parser parser
    ) {
        super(botToken);
        this.chatConfigService = chatConfigService;
        this.testDataTemplateService = testDataTemplateService;
        this.messageGenerator = messageGenerator;
        this.parser = parser;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        String messageText;
        Long chatId;

        if(update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText();
        } else {
            return;
        }

        if(!chatConfigService.isChatInit(chatId)) {
            chatConfigService.initChat(chatId);
            try {
                sendMessage(chatId, messageGenerator.generateStartMessage());
            } catch(TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                handleBotState(messageText, chatId);
            } catch(TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessage(Long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        execute(sendMessage);
    }

    private void handleBotState(String message, Long chatId) throws TelegramApiException {
        BotState state = chatConfigService.getBotState(chatId);
        if(message.equals(MainCommand.CANCEL)) {
            if(state == BotState.DEFAULT) {
                sendMessage(chatId, "Нет активной команды для отклонения");
            } else {
                chatConfigService.setBotState(chatId, BotState.DEFAULT);
                sendMessage(chatId, messageGenerator.generateCancelMessage());
                return;
            }
        }

        switch(state) {
            case DEFAULT: {
                if(message.equals(MainCommand.HELP)) {
                    sendMessage(chatId, messageGenerator.generateHelpMessage());
                } else if(message.equals(MainCommand.TEST_DATA)) {
                    chatConfigService.setBotState(chatId, BotState.TMP_WAIT_NAME);
                    sendMessage(chatId, "Введите название для шаблона");
                }
                break;
            }

            case TMP_WAIT_NAME: {
                chatConfigService.setTmpInUse(chatId, testDataTemplateService.setName(message));
                sendMessage(chatId, "Введите список полей");
                chatConfigService.setBotState(chatId, BotState.TMP_WAIT_LIST);
                break;
            }

            case TMP_WAIT_LIST: {
                BigInteger tmpId = chatConfigService.getTmpInUse(chatId);
                testDataTemplateService.setPattern(message, tmpId);
                JsonNode jsonNode = parser.parse(message);
                File file = new File(String.format("%s.json", testDataTemplateService.getTemplate(tmpId).get().getName()));
                try {
                    objectMapper.writeValue(file, jsonNode);
                    SendDocument sendDocument = new SendDocument(chatId.toString(), new InputFile(file));
                    execute(sendDocument);
                    chatConfigService.setBotState(chatId, BotState.DEFAULT);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
