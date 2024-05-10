package com.isacitra.authentication.modules.authmodule.provider;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthProvider {
    
    private static AuthProvider instance;
    private BCryptPasswordEncoder encoder;
    private AuthProvider(){
        encoder = new BCryptPasswordEncoder();
    }
    public static synchronized AuthProvider getInstance() {
        if (instance == null) {
            instance = new AuthProvider();
        }
        return instance;
    }
    public boolean matches(String rawPassword, String hashedPassword){
        return encoder.matches(rawPassword, hashedPassword);
    }
    public String encode(String raw){
        return encoder.encode(raw);
    }
}
