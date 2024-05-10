package com.isacitra.authentication.modules.authmodule.repository;

import com.isacitra.authentication.modules.authmodule.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findAuthUserByUsername(String username);
    AuthUser findAuthUserByEmail(String email);

    void deleteAuthUserByUsername(String username);
}
