package com.isacitra.authentication.modules.authmodule.service;

import com.isacitra.authentication.modules.authmodule.model.dto.EmailVerificationDTO;

public interface EmailService {
    void sendEmailVerification(EmailVerificationDTO emailVerificationSetting);
    boolean verifyEmail(String email);

}
