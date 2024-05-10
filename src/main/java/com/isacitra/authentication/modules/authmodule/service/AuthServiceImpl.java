package com.isacitra.authentication.modules.authmodule.service;

import com.isacitra.authentication.modules.authmodule.model.AuthUser;
import com.isacitra.authentication.modules.authmodule.model.dto.UserRegisterInfoDTO;
import com.isacitra.authentication.modules.authmodule.provider.AuthProvider;
import com.isacitra.authentication.modules.authmodule.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthUserRepository repository;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    private AuthUser authenticateByEmail(String email, String password) {
        AuthUser authUser = repository.findAuthUserByEmail(email);
        if(authUser == null){
            throw  new NoSuchElementException("Maaf email tidak ditemukan.");
        }
        if(! AuthProvider.getInstance().matches(password, authUser.getPassword())){ // bandingkan raw pass dengan hashed pass
            throw  new IllegalArgumentException("Maaf password tidak sesuai");
        }
        return authUser;
    }


    private AuthUser changePassByEmail(String email, String newPassword) {
        AuthUser authUser = repository.findAuthUserByEmail(email);
        if(authUser == null){
            throw  new NoSuchElementException("Maaf email tidak ditemukan");
        }
        authUser.setPassword(AuthProvider.getInstance().encode(newPassword));
        return  repository.save(authUser);
    }

    @Override
    public AuthUser authenticate(String identifier, String password) {
        return  authenticateByEmail(identifier, password);
    }

    @Override
    public AuthUser getUserData(String identifier) {
        return repository.findAuthUserByEmail(identifier);
    }

    private void checkPasswordMatching(String password, String passwordConfirmation){
        if(! password.equals(passwordConfirmation)) {
            throw new IllegalArgumentException("Password dan konfirmasi password tidak cocok.");
        }
    }

    @Override
    public AuthUser changePassword(String identifier, String password, String passwordConfirmation) {
        checkPasswordMatching(password, passwordConfirmation);
        return changePassByEmail(identifier, password);
    }

    @Override
    public boolean isEmail(String identifier) {
        return EMAIL_PATTERN.matcher(identifier).matches();
    }

    private void checkIsAlreadyExist(String email){
        if(repository.findAuthUserByEmail(email) != null){
            throw new DuplicateKeyException("email telah dipakai oleh user lain");
        };
    }

    @Override
    public AuthUser register(UserRegisterInfoDTO info) {
        checkPasswordMatching(info.getPassword(), info.getPasswordConfirmation());
        checkIsAlreadyExist(info.getEmail());
        AuthUser user = AuthUser.fromUserRegisterInfo(info);
        user.setPassword(AuthProvider.getInstance().encode(user.getPassword())); // Bcrypt
        return repository.save(AuthUser.fromUserRegisterInfo(info));
    }
}
