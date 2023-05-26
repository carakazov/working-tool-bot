package ru.bsc.workingtoolbot.model;


import java.math.BigInteger;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "chat_config")
@Accessors(chain = true)
@Data
public class ChatConfig {
    @Id
    private Long chatId;

    @Field(targetType = FieldType.STRING)
    private BotState state;

    private BigInteger tmpInUseId;

    @Field(targetType = FieldType.STRING)
    private TmpResultType tmpInUseResultType;
}
