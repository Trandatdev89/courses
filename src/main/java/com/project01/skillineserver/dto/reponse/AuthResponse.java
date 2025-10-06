package com.project01.skillineserver.dto.reponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private boolean authenticated;
    private String username;
    private Long userId;
}
