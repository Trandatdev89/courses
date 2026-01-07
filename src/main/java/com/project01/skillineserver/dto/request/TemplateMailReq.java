package com.project01.skillineserver.dto.request;

import com.project01.skillineserver.enums.EmailType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateMailReq {
    private Long id;
    private String subject;
    private String htmlContent;
    private EmailType type;
    private String language;
    private boolean active;
}
