package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterInfoDTO implements Serializable {
    private String password;
    private String passwordConfirmation;
    private String email;
    private String name;
}
