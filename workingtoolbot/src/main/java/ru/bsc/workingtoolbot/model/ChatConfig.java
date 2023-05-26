package ru.bsc.workingtoolbot.model;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_config")
public class ChatConfig {
    @Id
    private BigInteger id;
}
