package com.isacitra.authentication.modules.authmodule.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserInformationDTO implements Serializable {
    private Long userId;
    private String username;
    private String selfDescription;
    private String email;
    private String profilePhoto;
    private String name;



}
