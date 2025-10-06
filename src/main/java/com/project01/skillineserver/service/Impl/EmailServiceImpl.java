package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.request.VerifyAccountRequest;
import com.project01.skillineserver.service.EmailService;
import com.project01.skillineserver.utils.MailjetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailjetUtil mailjetUtil;

    @Override
    public void verifyAccount(VerifyAccountRequest verifyAccountRequest) {
        String httpUrlSend = verifyAccountRequest.getLinkUrl()+"/verify?userId="+verifyAccountRequest.getUserId()+"&token="+verifyAccountRequest.getToken();
        mailjetUtil.sendMailWithMailjet(verifyAccountRequest.getEmail(),
                "No Reply",
                "Verify Account",
                "Click to link of Verify Account: "+httpUrlSend,
                verifyAccountRequest.getToken());
    }
}