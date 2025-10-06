package com.project01.skillineserver.dto.request;

import com.project01.skillineserver.enums.TokenType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRequest {
    private String accessToken;
    private String refreshToken;
    private TokenType tokenType;
}
