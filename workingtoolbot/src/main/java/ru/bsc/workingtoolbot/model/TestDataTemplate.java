package ru.bsc.workingtoolbot.model;

import java.math.BigInteger;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "test_data_template")
public class TestDataTemplate {
    @Id
    private BigInteger id;

    private String name;

    private String tmpPattern;

    private String tmp;

    private Long chatId;
}
