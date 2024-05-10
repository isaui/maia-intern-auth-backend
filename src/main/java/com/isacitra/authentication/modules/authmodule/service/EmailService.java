package com.isacitra.authentication.modules.authmodule.service;

import com.isacitra.authentication.modules.authmodule.model.dto.TokenEmailVerificationDTO;
import com.isacitra.authentication.modules.authmodule.model.dto.RegisterEmailVerificationDTO;

public interface EmailService {
    void sendEmailVerification(TokenEmailVerificationDTO emailVerificationSetting);
    void sendRegistrationEmailVerification(RegisterEmailVerificationDTO info);

}
