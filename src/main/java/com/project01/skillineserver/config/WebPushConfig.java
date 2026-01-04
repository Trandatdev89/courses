package com.project01.skillineserver.config;

import jakarta.annotation.PostConstruct;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class WebPushConfig {

    @Value("${vapid.public.key}")
    private String publicKey;

    @Value("${vapid.private.key}")
    private String privateKey;

    @Value("${vapid.subject}")
    private String subject;

    @Bean
    public PushService pushService() {

        Security.addProvider(new BouncyCastleProvider());

        // Tạo PushService instance
        PushService pushService = new PushService();

        try {
            // Set VAPID keys
            pushService.setPublicKey(publicKey);
            pushService.setPrivateKey(privateKey);
            pushService.setSubject(subject);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize PushService", e);
        }

        return pushService;
    }

}