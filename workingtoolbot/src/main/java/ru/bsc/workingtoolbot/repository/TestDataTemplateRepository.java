package ru.bsc.workingtoolbot.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.bsc.workingtoolbot.model.TestDataTemplate;

public interface TestDataTemplateRepository extends MongoRepository<TestDataTemplate, BigInteger> {
    List<TestDataTemplate> findAllByChatId(Long chatId);

    Boolean existsByChatIdAndName(Long chatId, String name);

    List<TestDataTemplate> findAllByChatIdOrderByName(Long chatId);

    void deleteAllByChatId(Long chatId);
}
