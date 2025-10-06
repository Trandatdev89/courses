package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.request.VerifyAccountRequest;

public interface EmailService {
    void verifyAccount(VerifyAccountRequest verifyAccountRequest);
}
