package com.project01.skillineserver.dto.request;

import lombok.Data;
import nl.martijndwars.webpush.Subscription;

@Data
public class PushSubscription {
    private String endpoint;
    private Subscription.Keys keys;

    @Data
    public static class Keys {
        private String p256dh;
        private String auth;
    }

}
