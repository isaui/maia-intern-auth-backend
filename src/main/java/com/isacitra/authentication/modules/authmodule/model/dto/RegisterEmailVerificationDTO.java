package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterEmailVerificationDTO {
    private String recipientEmail;
    private String name;
    private String identifier;
    private String redirectLink;
}
