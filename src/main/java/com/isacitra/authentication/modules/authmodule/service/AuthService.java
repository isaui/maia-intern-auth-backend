package com.isacitra.authentication.modules.authmodule.service;


import com.isacitra.authentication.modules.authmodule.model.AuthUser;
import com.isacitra.authentication.modules.authmodule.model.dto.UserRegisterInfoDTO;

public interface AuthService {

    AuthUser authenticate(String identifier, String password);

    AuthUser getUserData(String identifier);
    AuthUser changePassword(String identifier, String password, String passwordConfirmation);

    boolean isEmail(String identifier);

    AuthUser register(UserRegisterInfoDTO info);
}
