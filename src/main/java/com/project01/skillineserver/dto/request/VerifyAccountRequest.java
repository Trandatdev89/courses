package com.project01.skillineserver.dto.request;

import com.project01.skillineserver.enums.EmailType;
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
    private String toEmail;
    private Long userId;
    private EmailType emailType;
    private String toName;
}