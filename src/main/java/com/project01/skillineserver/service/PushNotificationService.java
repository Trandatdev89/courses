package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.request.PushSubscription;

public interface PushNotificationService {
    void subscribe(Long userId,PushSubscription pushSubscription);
    void sendNotification(Long userId,String title,String body);
}
