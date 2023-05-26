package ru.bsc.workingtoolbot.bot;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bsc.workingtoolbot.main.Parser;
import ru.bsc.workingtoolbot.model.BotState;
import ru.bsc.workingtoolbot.model.TmpResultType;
import ru.bsc.workingtoolbot.service.ChatConfigService;
import ru.bsc.workingtoolbot.service.TestDataTemplateService;
import ru.bsc.workingtoolbot.utils.exception.ValidationException;


@Component
public class Bot extends TelegramLongPollingBot {
    private final ChatConfigService chatConfigService;
    private final TestDataTemplateService testDataTemplateService;
    private final MessageGenerator messageGenerator;
    private final Parser parser;
    private final KeyboardFactory keyboardFactory;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    @Value("${application.bot.username}")
    private String botUsername;

    @Autowired
    public Bot(
        @Value("${application.bot.token}") String botToken, ChatConfigService chatConfigService,
        TestDataTemplateService testDataTemplateService, MessageGenerator messageGenerator,
        Parser parser,
        KeyboardFactory keyboardFactory
    ) {
        super(botToken);
        this.chatConfigService = chatConfigService;
        this.testDataTemplateService = testDataTemplateService;
        this.messageGenerator = messageGenerator;
        this.parser = parser;
        this.keyboardFactory = keyboardFactory;
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
        } else if(update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            if(update.getCallbackQuery().getData().equals(CallbackType.TMP_MESSAGE)) {
                chatConfigService.setTmpInUseResultType(chatId, TmpResultType.MESSAGE);
            } else if(update.getCallbackQuery().getData().equals(CallbackType.TMP_FILE)) {
                chatConfigService.setTmpInUseResultType(chatId, TmpResultType.FILE);

            } else {
                return;
            }
            chatConfigService.setBotState(chatId, BotState.TMP_WAIT_NAME);
            try {
                sendMessage(chatId, "Введите название для шаблона");
            } catch(TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
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

    private void sendMessage(Long chatId, String text, ReplyKeyboard keyboard) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        sendMessage.setReplyMarkup(keyboard);
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
                } else if(message.equals(MainCommand.TEST_DATA_CREATE)) {
                    sendMessage(chatId, "В каком виде хотите получить результат?", keyboardFactory.getChooseTesDataTypeKeyboard());
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
                try {
                    JsonNode jsonNode = parser.parse(message);
                    if(chatConfigService.getTmpInUseResultType(chatId).equals(TmpResultType.MESSAGE)) {
                        sendMessage(chatId, jsonNode.toPrettyString());
                    } else {
                        testDataTemplateService.setPattern(message, tmpId);
                        File file = new File(String.format("%s.json", testDataTemplateService.getTemplate(tmpId).get().getName()));
                        objectMapper.writeValue(file, jsonNode);
                        SendDocument sendDocument = new SendDocument(chatId.toString(), new InputFile(file));
                        execute(sendDocument);
                    }
                    chatConfigService.setBotState(chatId, BotState.DEFAULT);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                } catch(ValidationException e) {
                    sendMessage(chatId, e.getMessage());
                }

            }
        }
    }
}
