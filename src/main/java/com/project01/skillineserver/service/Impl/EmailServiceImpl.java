package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.request.VerifyAccountRequest;
import com.project01.skillineserver.entity.EmailTemplate;
import com.project01.skillineserver.enums.EmailType;
import com.project01.skillineserver.repository.TemplateMailRepository;
import com.project01.skillineserver.service.EmailService;
import com.project01.skillineserver.utils.MailjetUtil;
import com.project01.skillineserver.utils.MapUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailjetUtil mailjetUtil;
    private final TemplateMailRepository templateMailRepository;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void verifyAccount(VerifyAccountRequest verifyAccountRequest) throws IllegalAccessException {

        EmailTemplate emailTemplate = templateMailRepository.findByType(EmailType.WELCOME).orElse(null);

        assert emailTemplate != null;
        String resultRender = renderTemplate(emailTemplate.getHtmlContent(), MapUtil.extractInfo(verifyAccountRequest));
        mailjetUtil.sendMailWithMailjet(verifyAccountRequest.getEmail(),
                "No Reply",
                "Verify Account",
                resultRender,
                verifyAccountRequest.getToken());
    }

    public String renderTemplate(String html, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        return templateEngine.process(html, context);
    }
}