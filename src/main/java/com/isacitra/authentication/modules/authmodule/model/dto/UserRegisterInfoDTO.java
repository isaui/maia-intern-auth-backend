package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserRegisterInfoDTO implements Serializable {
    private String username;
    private String password;
    private String passwordConfirmation;
    private String email;
    private String name;
    private String photoProfile;
}
