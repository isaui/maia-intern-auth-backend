package com.isacitra.authentication.modules.authmodule.provider;


import com.isacitra.authentication.common.enums.RedisConstants;
import com.isacitra.authentication.common.provider.RedisProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.isacitra.authentication.modules.authmodule.model.AuthUser;
import com.isacitra.authentication.modules.authmodule.model.dto.UserInformationDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.concurrent.TimeUnit;

@Component
public class JwtProvider {

    @Autowired
    private RedisProvider redisProvider;
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String createJwtToken(String subject){
        Claims claims = Jwts.claims();
        claims.setSubject(subject);
        return  Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public  String generateKeyAuthentication(String token){
        return  RedisConstants.AUTH_TOKEN.getPrefix()+token;
    }

    public String createAuthenticationTokenForUser(UserInformationDTO user){
        String token = createJwtToken(user.getEmail());
        String key = generateKeyAuthentication(token);
        redisProvider.getRedisTemplate().opsForValue().set
                (key, user.getEmail(), RedisProvider.getExpirationTimeMs(), TimeUnit.MILLISECONDS);
        return token;
    }

}
