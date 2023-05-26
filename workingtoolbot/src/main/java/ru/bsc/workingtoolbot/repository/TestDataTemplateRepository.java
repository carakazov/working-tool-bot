package ru.bsc.workingtoolbot.repository;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.bsc.workingtoolbot.model.TestDataTemplate;

public interface TestDataTemplateRepository extends MongoRepository<TestDataTemplate, BigInteger> {
}
