package ru.bsc.workingtoolbot.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bsc.workingtoolbot.model.BotState;
import ru.bsc.workingtoolbot.model.ChatConfig;
import ru.bsc.workingtoolbot.model.TmpResultType;
import ru.bsc.workingtoolbot.repository.ChatConfigRepository;

@Service
@RequiredArgsConstructor
public class ChatConfigService {
    private final ChatConfigRepository chatConfigRepository;

    public boolean isChatInit(Long chatId) {
        return chatConfigRepository.existsById(chatId);
    }

    public void initChat(Long chatId) {
        chatConfigRepository.save(new ChatConfig().setChatId(chatId).setState(BotState.DEFAULT));
    }

    public List<String> getFilesInUse(Long chatId) {
        ChatConfig filesInUse = chatConfigRepository.findById(chatId).get();
        return filesInUse.getFilesInUse();
    }

    public void setBotState(Long chatId, BotState botState) {
        ChatConfig chatConfig = chatConfigRepository.findById(chatId).get();
        chatConfig.setState(botState);
        if(botState == BotState.DEFAULT) {
            chatConfig.setFilesInUse(Collections.emptyList());
            chatConfig.setHelpMark(null);
            chatConfig.setTmpInUseId(null);
            chatConfig.setTmpInUseResultType(null);
        }
        chatConfigRepository.save(chatConfig);
    }

    public void addFileContent(Long chatId, String content) {
        ChatConfig chatConfig = chatConfigRepository.findById(chatId).get();
        List<String> contentList = chatConfig.getFilesInUse();
        if(contentList == null) {
            contentList = new ArrayList<>();
        }
        contentList.add(content);
        chatConfig.setFilesInUse(contentList);
        chatConfigRepository.save(chatConfig);
    }

    public void setHelpMark(Long chatId, Boolean helpMark) {
        ChatConfig chatConfig = chatConfigRepository.findById(chatId).get();
        chatConfig.setHelpMark(helpMark);
        chatConfigRepository.save(chatConfig);
    }

    public Boolean getHelpMark(Long chatId) {
        return chatConfigRepository.findById(chatId).get().getHelpMark();
    }

    public void setTmpInUse(Long chatId, BigInteger tmpId) {
        ChatConfig chatConfig = chatConfigRepository.findById(chatId).get();
        chatConfig.setTmpInUseId(tmpId);
        chatConfigRepository.save(chatConfig);
    }

    public void setTmpInUseResultType(Long chatId, TmpResultType resultType) {
        ChatConfig chatConfig = chatConfigRepository.findById(chatId).get();
        chatConfig.setTmpInUseResultType(resultType);
        chatConfigRepository.save(chatConfig);
    }

    public BotState getBotState(Long chatId) {
        return chatConfigRepository.findById(chatId).get().getState();
    }

    public BigInteger getTmpInUse(Long chatId) {
        return chatConfigRepository.findById(chatId).get().getTmpInUseId();
    }

    public TmpResultType getTmpInUseResultType(Long chatId) {
        return chatConfigRepository.findById(chatId).get().getTmpInUseResultType();
    }
}
