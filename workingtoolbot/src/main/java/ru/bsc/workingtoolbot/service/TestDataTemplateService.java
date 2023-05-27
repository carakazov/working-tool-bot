package ru.bsc.workingtoolbot.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bsc.workingtoolbot.model.TestDataTemplate;
import ru.bsc.workingtoolbot.repository.TestDataTemplateRepository;
import ru.bsc.workingtoolbot.utils.exception.LogicException;
import ru.bsc.workingtoolbot.utils.exception.ValidationException;

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

    public String generateTestDataTemplatesMessage(Long chatId) {
        List<TestDataTemplate> templates = repository.findAllByChatId(chatId);
        StringBuilder sb = new StringBuilder();
        AtomicInteger i = new AtomicInteger(1);
        templates.forEach(t -> sb.append(i.getAndIncrement()).append(".  ").append(t.getName()).append("\n"));
        return sb.toString();
    }

    public TestDataTemplate getTemplateByChatIdAndOrderingId(Long chatId, Integer orderingId) {
        return repository.findAllByChatId(chatId).get(orderingId - 1);
    }

    public List<TestDataTemplate> findAllByChatId(Long chatId) {
        return repository.findAllByChatId(chatId);
    }

    public Boolean existsByChatIdAndName(Long chatId, String name) {
        return repository.existsByChatIdAndName(chatId, name);
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
        if(existsByChatIdAndName(chatId, name)) {
            throw new ValidationException("У вас уже есть шаблон с таким именем. Придумайте другое.");
        }
        return setName(name, null, chatId);
    }

}
