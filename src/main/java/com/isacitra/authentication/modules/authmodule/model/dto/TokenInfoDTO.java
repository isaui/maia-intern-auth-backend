package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter

public class TokenInfoDTO implements Serializable {
    private String token;
    private boolean isAuthenticated;

    public TokenInfoDTO(String token) {
        this.token = token;
        this.isAuthenticated = false;
    }
    public  TokenInfoDTO(String token, boolean isAuthenticated){
        this.token = token;
        this.isAuthenticated = isAuthenticated;
    }
    public static class TokenInfoDTOBuilder {
        private String token;
        private boolean isAuthenticated;

        public TokenInfoDTOBuilder token(String token) {
            this.token = token;
            return this;
        }

        public TokenInfoDTOBuilder isAuthenticated(boolean isAuthenticated) {
            this.isAuthenticated = isAuthenticated;
            return this;
        }

        public TokenInfoDTO build() {
            return new TokenInfoDTO(token, isAuthenticated);
        }
    }

}

