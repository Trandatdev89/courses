package com.project01.skillineserver.mapper;

import com.project01.skillineserver.dto.reponse.NotificationResponse;
import com.project01.skillineserver.entity.NotificationEntity;
import com.project01.skillineserver.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final DateUtil dateUtil;

    public NotificationResponse convertNotificationEntity(NotificationEntity notificationEntity,String avatarUrl){
        return NotificationResponse.builder()
                .id(notificationEntity.getId())
                .title(notificationEntity.getTitle())
                .content(notificationEntity.getContent())
                .avatarUrl(avatarUrl)
                .nameUser(notificationEntity.getNameUser())
                .isActive(notificationEntity.isActive())
                .linkAttachment(notificationEntity.getLinkAttachment())
                .createAt(dateUtil.format(notificationEntity.getCreateAt()))
                .timePush(dateUtil.format(notificationEntity.getTimePush()))
                .build();
    }
}
