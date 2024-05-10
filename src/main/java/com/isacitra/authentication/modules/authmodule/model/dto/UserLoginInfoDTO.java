package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserLoginInfoDTO implements Serializable {
    private String identifier;
    private String password;
    public UserLoginInfoDTO(){}
}
