package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.NotificationResponse;
import com.project01.skillineserver.dto.request.NotificationReq;
import com.project01.skillineserver.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getListNotification(NotificationType notificationType);
    void createNotification(NotificationReq notificationReq);
    void inActiveNotification();
}
