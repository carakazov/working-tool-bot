package ru.bsc.workingtoolbot.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bsc.workingtoolbot.model.TestDataTemplate;
import ru.bsc.workingtoolbot.repository.TestDataTemplateRepository;
import ru.bsc.workingtoolbot.utils.exception.LogicException;

@Service
@RequiredArgsConstructor
public class TestDataTemplateService {
    private final TestDataTemplateRepository repository;

    private TestDataTemplate createTmp() {
        return repository.save(new TestDataTemplate());
    }

    public Optional<TestDataTemplate> getTemplate(BigInteger id) {
        return repository.findById(id);
    }

    public List<TestDataTemplate> findAllByChatId(Long chatId) {
        return repository.findAllByChatId(chatId);
    }

    public void addContent(BigInteger id, String tmpTemplate, String tmpContent) {
        TestDataTemplate testDataTemplate = getTemplate(id).orElseThrow(() -> new LogicException("Произошла ошибка на сервере"));
        testDataTemplate.setTmpPattern(tmpTemplate);
        testDataTemplate.setTmp(tmpContent);
        repository.save(testDataTemplate);
    }

    public BigInteger setName(String name, BigInteger id, Long chatId) {
        TestDataTemplate testDataTemplate;
        if(id == null) {
            testDataTemplate = createTmp();
        } else {
            testDataTemplate = getTemplate(id).get();
        }
        testDataTemplate.setName(name);
        testDataTemplate.setChatId(chatId);
        repository.save(testDataTemplate);
        return testDataTemplate.getId();
    }

    public BigInteger setName(String name, Long chatId) {
        return setName(name, null, chatId);
    }

}
