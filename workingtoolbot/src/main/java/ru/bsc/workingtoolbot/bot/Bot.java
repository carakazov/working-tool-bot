package ru.bsc.workingtoolbot.bot;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bsc.workingtoolbot.dto.ClassDto;
import ru.bsc.workingtoolbot.dto.TestClassesDto;
import ru.bsc.workingtoolbot.main.JavaReader;
import ru.bsc.workingtoolbot.main.Parser;
import ru.bsc.workingtoolbot.main.TestClassCreator;
import ru.bsc.workingtoolbot.main.impl.JavaParser;
import ru.bsc.workingtoolbot.model.BotState;
import ru.bsc.workingtoolbot.model.TestDataTemplate;
import ru.bsc.workingtoolbot.model.TmpResultType;
import ru.bsc.workingtoolbot.service.ChatConfigService;
import ru.bsc.workingtoolbot.service.TestDataTemplateService;
import ru.bsc.workingtoolbot.utils.exception.ValidationException;


@Component
public class Bot extends TelegramLongPollingBot {
    private final JavaParser javaParser;
    private final ChatConfigService chatConfigService;
    private final TestDataTemplateService testDataTemplateService;
    private final MessageGenerator messageGenerator;
    private final Parser parser;
    private final KeyboardFactory keyboardFactory;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    private final JavaReader javaReader;
    private final TestClassCreator testClassCreator;
    @Value("${application.bot.username}")
    private String botUsername;

    @Autowired
    public Bot(
        @Value("${application.bot.token}") String botToken, JavaParser javaParser, ChatConfigService chatConfigService,
        TestDataTemplateService testDataTemplateService, MessageGenerator messageGenerator,
        Parser parser,
        KeyboardFactory keyboardFactory,
        JavaReader javaReader,
        TestClassCreator testClassCreator
    ) {
        super(botToken);
        this.javaParser = javaParser;
        this.chatConfigService = chatConfigService;
        this.testDataTemplateService = testDataTemplateService;
        this.messageGenerator = messageGenerator;
        this.parser = parser;
        this.keyboardFactory = keyboardFactory;
        this.javaReader = javaReader;
        this.testClassCreator =testClassCreator;
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
        String messageText = "";
        Long chatId;
        File file = null;
        String callbackData = "";

        if(update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText();
            if(messageText == null) {
                messageText = "";
            }
            Document document = update.getMessage().getDocument();
            if(document != null) {
                GetFile getFile = new GetFile(document.getFileId());
                String filePath;
                try {
                    filePath = execute(getFile).getFilePath();
                    file = downloadFile(filePath);
                } catch(TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if(update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            callbackData = update.getCallbackQuery().getData();
            try {
                if(callbackData.equals(CallbackType.TMP_MESSAGE)) {
                    chatConfigService.setTmpInUseResultType(chatId, TmpResultType.MESSAGE);
                    chatConfigService.setBotState(chatId, BotState.TMP_WAIT_NAME);
                    sendMessage(chatId, "Введите название для шаблона");
                    return;
                } else if(callbackData.equals(CallbackType.TMP_FILE)) {
                    chatConfigService.setTmpInUseResultType(chatId, TmpResultType.FILE);
                    chatConfigService.setBotState(chatId, BotState.TMP_WAIT_NAME);
                    sendMessage(chatId, "Введите название для шаблона");
                    return;
                } else if(callbackData.equals(CallbackType.TMP_LIST_FILE)) {
                    chatConfigService.setTmpInUseResultType(chatId, TmpResultType.FILE);
                    sendTmpListMessage(chatId);
                } else if(callbackData.equals(CallbackType.TMP_LIST_MESSAGE)) {
                    chatConfigService.setTmpInUseResultType(chatId, TmpResultType.MESSAGE);
                    sendTmpListMessage(chatId);
                    return;
                }
            } catch(TelegramApiException e) {
                throw new RuntimeException(e);
            }
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
                handleBotState(messageText, chatId, file, callbackData);
            } catch(TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendTmpListMessage(Long chatId) throws TelegramApiException {
        String tmp = testDataTemplateService.generateTestDataTemplatesMessage(chatId);
        if(tmp.isEmpty()) {
            sendMessage(chatId, "Сохраненных шаблонов нет");
            return;
        }
        sendMessage(chatId, String.format("Выберите необходимый шаблон:%n%s", tmp));
        chatConfigService.setBotState(chatId, BotState.TMP_LIST_WAIT_FOR_CHOOSING);
        return;
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

    private void handleBotState(String message, Long chatId, File file, String callbackData) throws
        TelegramApiException {
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
                    sendMessage(
                        chatId,
                        "В каком виде хотите получить результат?",
                        keyboardFactory.getChooseTestDataTypeKeyboard(false)
                    );
                } else if(message.equals(MainCommand.TEST_CLASSES_GENERATE)) {
                    sendMessage(chatId, "Загрузите классы моделей и dto");
                    chatConfigService.setBotState(chatId, BotState.TC_WAIT_UPLOAD);
                } else if(message.equals(MainCommand.TEST_DATA_LIST)) {
                    sendMessage(
                        chatId,
                        "В каком виде хотите получить результат?",
                        keyboardFactory.getChooseTestDataTypeKeyboard(true)
                    );
                }
                break;
            }

            case TMP_LIST_WAIT_FOR_CHOOSING: {
                try {
                    Integer orderingId = Integer.valueOf(message);
                    TestDataTemplate template = testDataTemplateService.getTemplateByChatIdAndOrderingId(chatId, orderingId);
                    JsonNode jsonNode = objectMapper.readTree(template.getTmp());
                    generateJsonTmpMessage(chatId, jsonNode, template.getName());
                } catch(IOException e) {
                    sendMessage(chatId, "При обработке файла произошла ошибка");
                    chatConfigService.setBotState(chatId, BotState.DEFAULT);
                } catch(Exception e) {
                    sendMessage(chatId, "Введите число из списка");
                }
                break;
            }

            case TC_WAIT_UPLOAD: {
                if(file != null) {
                    chatConfigService.addFileContent(chatId, javaParser.getContent(file));
                    if(chatConfigService.getHelpMark(chatId) == null || Boolean.FALSE.equals(chatConfigService.getHelpMark(
                        chatId))) {
                        chatConfigService.setHelpMark(chatId, Boolean.TRUE);
                        sendMessage(chatId, "Все классы загружены?", keyboardFactory.getChooseUploadFinished());
                    }
                } else {
                    if(callbackData.equals(CallbackType.TC_UPLOAD_FINISHED)) {
                        List<String> files = chatConfigService.getFilesInUse(chatId);
                        List<ClassDto> classDtos = javaReader.readClasses(files);
                        TestClassesDto testClassesDto = testClassCreator.createClasses(classDtos);
                        try {
                            File constants = new File("TestDataConstants.java");
                            objectMapper.writeValue(constants, testClassesDto.getTestDataConstants());
                            SendDocument sendConstants = new SendDocument(chatId.toString(), new InputFile(constants));
                            execute(sendConstants);

                            File functions = new File("FabricFunctionUtils.java");
                            objectMapper.writeValue(constants, testClassesDto.getTestDataConstants());
                            SendDocument sendFunctions = new SendDocument(chatId.toString(), new InputFile(functions));
                            execute(sendFunctions);
                        } catch(IOException exception) {
                            throw new RuntimeException(exception);
                        }
                        chatConfigService.setBotState(chatId, BotState.DEFAULT);
                    } else if(callbackData.equals(CallbackType.TC_UPLOAD_NOT_FINISHED)) {
                        sendMessage(chatId, "Загрузите классы моделей и dto");
                        chatConfigService.setHelpMark(chatId, false);
                    }
                }
                break;
            }

            case TMP_WAIT_NAME: {

                try {
                    chatConfigService.setTmpInUse(chatId, testDataTemplateService.setName(message, chatId));
                    sendMessage(chatId, "Введите список полей");
                    chatConfigService.setBotState(chatId, BotState.TMP_WAIT_LIST);
                    break;
                } catch(ValidationException exception) {
                    sendMessage(chatId, exception.getMessage());
                    break;
                }
            }

            case TMP_WAIT_LIST: {
                BigInteger tmpId = chatConfigService.getTmpInUse(chatId);
                try {
                    JsonNode jsonNode = parser.parse(message, chatId);
                    testDataTemplateService.addContent(tmpId, message, jsonNode.toString());
                    generateJsonTmpMessage(chatId, jsonNode, testDataTemplateService.getTemplate(tmpId).get().getName());
                } catch(IOException e) {
                    sendMessage(chatId, "При обработке файла произошла ошибка");
                    chatConfigService.setBotState(chatId, BotState.DEFAULT);
                } catch(ValidationException e) {
                    sendMessage(chatId, e.getMessage());
                } catch(LogicException e) {
                    sendMessage(chatId, e.getMessage());
                }
            }
        }
    }

    private void generateJsonTmpMessage(Long chatId, JsonNode jsonNode, String tmpName) throws
        TelegramApiException,
        IOException {
        if(chatConfigService.getTmpInUseResultType(chatId).equals(TmpResultType.MESSAGE)) {
            sendMessage(chatId, jsonNode.toPrettyString());
        } else {
            File fileTmp = new File(String.format(
                "%s.json",
                tmpName
            ));
            objectMapper.writeValue(fileTmp, jsonNode);
            SendDocument sendDocument = new SendDocument(chatId.toString(), new InputFile(fileTmp));
            execute(sendDocument);
        }
        chatConfigService.setBotState(chatId, BotState.DEFAULT);
    }
}
