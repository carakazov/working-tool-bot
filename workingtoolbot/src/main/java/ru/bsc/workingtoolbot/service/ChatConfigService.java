package ru.bsc.workingtoolbot.service;

import java.math.BigInteger;

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

    public void setBotState(Long chatId, BotState botState) {
        ChatConfig chatConfig = chatConfigRepository.findById(chatId).get();
        chatConfig.setState(botState);
        chatConfigRepository.save(chatConfig);
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
