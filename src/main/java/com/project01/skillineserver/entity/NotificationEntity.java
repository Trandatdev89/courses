package com.project01.skillineserver.entity;

import com.project01.skillineserver.enums.NotificationType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Document(collection = "notification")
public class NotificationEntity {

    @MongoId(FieldType.STRING)
    private String id;
    private NotificationType notificationType;
    private String content;
    private String userId;
    private String nameUser;
    private String title;
    private String linkAttachment;
    private boolean isActive;
    private Instant timePush;
    private Instant createdAt;

}
