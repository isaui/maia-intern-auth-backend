package com.isacitra.authentication.modules.authmodule.provider;

import com.isacitra.authentication.common.enums.EmailVerificationConstants;
import com.isacitra.authentication.common.enums.RedisConstants;
import com.isacitra.authentication.modules.authmodule.model.dto.TokenInfoDTO;
import com.isacitra.authentication.common.provider.RedisProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.isacitra.authentication.modules.authmodule.model.dto.UserRegisterInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class EmailVerificationProvider {
    @Autowired
    RedisProvider redisProvider;

    public String createEmailAuthenticationToken(String type, String email) {
        if(! EmailVerificationConstants.contains(type)){
            throw new IllegalArgumentException("perintah invalid");
        }
        String key = generateKey(type, email);
        String token = UUID.randomUUID().toString().substring(0, 6);
        try {
            redisProvider.getRedisTemplate().opsForValue().set(key,
                    redisProvider.getObjectMapper().writeValueAsString
                            (new TokenInfoDTO(token)),
                    RedisProvider.getEmailExpirationTimeMs(), TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    public String findExistingRegisterTokenByEmail(String email){
        return redisProvider.get(generateKey(EmailVerificationConstants.
                GET_TOKEN_REGISTER_BY_EMAIL.getType(), email));
    }

    public void setRegisterTokenByEmail(String email, String token){
        String key = generateKey(EmailVerificationConstants.
                GET_TOKEN_REGISTER_BY_EMAIL.getType(), email);
        redisProvider.getRedisTemplate().opsForValue().set(key, token,
                10 * RedisProvider.getEmailExpirationTimeMs(), TimeUnit.MILLISECONDS);
    }

    public  String createRegisterURI(UserRegisterInfoDTO registerInfo){
        String identifier =  UUID.randomUUID().toString();
        String key = generateKey(EmailVerificationConstants.REGISTER.getType(), identifier);
        String oldIdentifier = findExistingRegisterTokenByEmail(registerInfo.getEmail());
        removeRegisterURI(oldIdentifier);
        setRegisterTokenByEmail(registerInfo.getEmail(), identifier);
        saveRegisterURI(key, registerInfo);
        return identifier;
    }

    private void saveRegisterURI(String key, UserRegisterInfoDTO registerInfo){
        try {
            redisProvider.getRedisTemplate().opsForValue().set(key,
                    redisProvider.getObjectMapper().writeValueAsString
                            (registerInfo), 10 * RedisProvider.getEmailExpirationTimeMs(), TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeRegisterURI(String url){
        if(url == null){
            return;
        }
        redisProvider.revoke(generateKey(EmailVerificationConstants.REGISTER.getType(), url));
    }

    public  UserRegisterInfoDTO getRegisterCacheFromURI(String url){
        String key = generateKey(EmailVerificationConstants.REGISTER.getType(), url);
        String value = redisProvider.get(key);
        if(value == null){
            return  null;
        }
        try {
            return redisProvider.getObjectMapper().readValue(value, UserRegisterInfoDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateKey(String type, String identifier){
        return switch (EmailVerificationConstants.valueOf(type)) {
            case REGISTER -> RedisConstants.VERIFICATION_REGISTER_EMAIL.getPrefix() + identifier;
            case CHANGE_PASS -> RedisConstants.VERIFICATION_CHANGE_PASSWORD.getPrefix() + identifier;
            case GET_TOKEN_REGISTER_BY_EMAIL -> RedisConstants.TOKEN_REGISTER_EMAIL.getPrefix() + identifier;
            default -> throw new IllegalArgumentException("Tipe verifikasi email tidak valid: " + type);
        };
    }

    public boolean verifyTokenEmail(String type, String email, String token){
        String key = generateKey(type, email);
        String data = redisProvider.get(key);
        if(data == null){
            return false;
        }
        try {
            TokenInfoDTO tokenInfo = redisProvider.
                    getObjectMapper().readValue(data, TokenInfoDTO.class);
            if(! tokenInfo.getToken().equals(token)){
                return  false;
            }
            tokenInfo.setAuthenticated(true);
            redisProvider.getRedisTemplate().opsForValue().set(key,
                    redisProvider.getObjectMapper().writeValueAsString
                            (tokenInfo),
                    RedisProvider.getEmailExpirationTimeMs(), TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return  false;
        }
        return  true;

    }

    public boolean isEmailVerified(String type, String email) {
        String key = generateKey(type, email);
        String data = redisProvider.get(key);
        System.out.println("-----------------------========================");
        System.out.println(data);
        if (data == null) {
            return false;
        }
        try {
            TokenInfoDTO verificationObject =
                    redisProvider.getObjectMapper().readValue(data, TokenInfoDTO.class);
            return verificationObject.isAuthenticated();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
