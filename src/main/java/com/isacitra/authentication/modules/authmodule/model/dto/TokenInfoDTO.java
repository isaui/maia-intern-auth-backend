package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class TokenInfoDTO implements Serializable {
    private String token;
    private boolean isAuthenticated;
    public TokenInfoDTO() {
    }
    public TokenInfoDTO(String token) {
        this.token = token;
        this.isAuthenticated = false;
    }
}

