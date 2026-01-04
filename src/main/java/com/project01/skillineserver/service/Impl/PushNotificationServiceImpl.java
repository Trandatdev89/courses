package com.project01.skillineserver.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project01.skillineserver.dto.request.PushSubscription;
import com.project01.skillineserver.entity.UserSubscription;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.UserSubscriptionRepository;
import com.project01.skillineserver.service.PushNotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final ObjectMapper objectMapper;
    private final PushService pushService;

    @Value("${vapid.private.key}")
    private String loadPrivateKey;

    @Value("${vapid.public.key}")
    private String loadPublicKey;

    @Value("${vapid.subject}")
    private String subject;


    @Override
    public void subscribe(Long userId, PushSubscription pushSubscription) {

        if (pushSubscription == null) {
            throw new AppException(ErrorCode.INTERNAL_SERVER);
        }

        UserSubscription userSubscription = UserSubscription.builder()
                .auth(pushSubscription.getKeys().auth)
                .endpoint(pushSubscription.getEndpoint())
                .userId(userId)
                .p256dh(pushSubscription.getKeys().p256dh)
                .build();
        userSubscriptionRepository.save(userSubscription);
    }

    @Override
    public void sendNotification(Long userId, String title, String body) {
        List<UserSubscription> userSubscriptions = userSubscriptionRepository.findByUserId(userId);
        try {
            for (UserSubscription userSubscription : userSubscriptions) {
                Map<String, Object> infoNotification = new HashMap<>();
                infoNotification.put("title", title);
                infoNotification.put("body", body);
                String payload = objectMapper.writeValueAsString(infoNotification);

                Subscription.Keys keys = new Subscription.Keys(
                        userSubscription.getP256dh(),
                        userSubscription.getAuth()
                );

                Subscription subscription = new Subscription(
                        userSubscription.getEndpoint(),
                        keys
                );

                Notification notification =
                        new Notification(subscription, payload);
                pushService.send(notification);

            }
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }

    @PostConstruct
    public void init() throws Exception {
        pushService.setPublicKey(Utils.loadPublicKey(loadPublicKey));
        pushService.setPrivateKey(Utils.loadPrivateKey(loadPrivateKey));
        pushService.setSubject(subject);
    }
}
