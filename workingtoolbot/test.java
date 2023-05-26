package com.ostapdev.crmlabserver.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

@Data
@Table(name = "chat")
@Entity
@Accessors(chain = true)
public class ChatEntity extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_1_id")
    @NotNull
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "user_2_id")
    @NotNull
    private UserEntity user2;

    @OneToMany(mappedBy = "chat")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<ChatMessageEntity> messages;
}