package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.request.TemplateMailReq;
import com.project01.skillineserver.entity.EmailTemplate;
import com.project01.skillineserver.repository.TemplateMailRepository;
import com.project01.skillineserver.service.TemplateMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateMailServiceImpl implements TemplateMailService {

    private final TemplateMailRepository templateMailRepository;

    @Override
    public void saveTemplateMail(TemplateMailReq templateMailReq) {
        boolean isUpdate = templateMailReq.getId()!=null;

        EmailTemplate emailTemplate = templateMailRepository
                .findByType(templateMailReq.getType())
                .orElseGet(EmailTemplate::new);

        emailTemplate.setType(templateMailReq.getType());
        emailTemplate.setHtmlContent(templateMailReq.getHtmlContent());
        emailTemplate.setSubject(templateMailReq.getSubject());
        emailTemplate.setActive(!isUpdate || templateMailReq.isActive());
        emailTemplate.setLanguage(templateMailReq.getLanguage());

        templateMailRepository.save(emailTemplate);
    }
}
