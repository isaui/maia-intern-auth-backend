package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class EmailVerificationDTO implements Serializable {
    private String recipientEmail;
    private String token;
    private String content;
    private String subject;
    private String name;
}
