package com.project01.skillineserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "user_devices")
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    @Column(unique = true)
    private String deviceId;

    private String ipAddress; // Lưu IP để tham khảo thôi
    private String userAgent;  // Lưu để hiển thị cho user biết

    private LocalDateTime firstLogin;
    private LocalDateTime lastLogin;

    private boolean isActive;
}
