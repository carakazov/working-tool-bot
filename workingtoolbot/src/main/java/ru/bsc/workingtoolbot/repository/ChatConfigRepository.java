package ru.bsc.workingtoolbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.bsc.workingtoolbot.model.ChatConfig;

public interface ChatConfigRepository extends MongoRepository<ChatConfig, Long> {
}
