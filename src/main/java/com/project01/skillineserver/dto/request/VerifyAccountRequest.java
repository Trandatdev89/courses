package com.project01.skillineserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VerifyAccountRequest {
    private String token;
    private String linkUrl;
    private String email;
    private Long userId;
}