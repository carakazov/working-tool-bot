package ru.bsc.workingtoolbot.service;

import java.math.BigInteger;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bsc.workingtoolbot.model.TestDataTemplate;
import ru.bsc.workingtoolbot.repository.TestDataTemplateRepository;

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

    public String generateTestDataTemplatesMessage() {
        return "";
    }

    public BigInteger setName(String name, BigInteger id) {
        TestDataTemplate testDataTemplate;
        if(id == null) {
            testDataTemplate = createTmp();
        } else {
            testDataTemplate = getTemplate(id).get();
        }
        testDataTemplate.setName(name);
        repository.save(testDataTemplate);
        return testDataTemplate.getId();
    }

    public BigInteger setName(String name) {
        return setName(name, null);
    }

    public void setPattern(String pattern, BigInteger id) {
        TestDataTemplate testDataTemplate = getTemplate(id).get();
        testDataTemplate.setTmpPattern(pattern);
        repository.save(testDataTemplate);
    }

    public void setTmp(JsonNode tmp, BigInteger id) {
        TestDataTemplate testDataTemplate = getTemplate(id).get();
        testDataTemplate.setTmp(tmp);
        repository.save(testDataTemplate);
    }
}
