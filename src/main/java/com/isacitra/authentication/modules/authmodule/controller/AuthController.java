package com.isacitra.authentication.modules.authmodule.controller;

import com.isacitra.authentication.common.enums.EmailVerificationType;
import com.isacitra.authentication.common.enums.URI;
import com.isacitra.authentication.modules.authmodule.model.AuthUser;
import com.isacitra.authentication.modules.authmodule.model.dto.*;
import com.isacitra.authentication.modules.authmodule.provider.EmailVerificationProvider;
import com.isacitra.authentication.modules.authmodule.provider.JwtProvider;
import com.isacitra.authentication.common.provider.RedisProvider;
import com.isacitra.authentication.modules.authmodule.service.AuthService;
import com.isacitra.authentication.modules.authmodule.service.EmailServiceImpl;
import com.isacitra.authentication.common.util.ResponseHandler;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    private ExecutorService virtualExecutor;
    @Autowired
    AuthService authService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired private EmailVerificationProvider emailAuthenticationProvider;
    @Autowired private RedisProvider redisProvider;

    AuthController(){
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request){
        Map<String, Object> data = new HashMap<>();
        String token = extractTokenFromHeader(request.getHeader("Authorization"));
        if(token == null){
            return  ResponseHandler.generateResponse("Tidak ada token",
                    HttpStatus.BAD_REQUEST, data);
        }
        String key = jwtProvider.generateKeyAuthentication(token);
        String existEmail = redisProvider.get(key);
        if(existEmail != null){
            redisProvider.revoke(key);
            AuthUser authUser = authService.getUserData(existEmail);
            data.put("token",jwtProvider.createAuthenticationTokenForUser(authUser.toUserInformationDTO()));
            return ResponseHandler.generateResponse("Session berhasil diperbarui",
                    HttpStatus.ACCEPTED, data);
        }
        else {
            return ResponseHandler.generateResponse("Session habis", HttpStatus.UNAUTHORIZED, data);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> postLogin(@RequestBody UserLoginInfoDTO info) {
        Map<String, Object> data = new HashMap<>();
        try{
            data.put("token", getToken(info.getIdentifier(), info.getPassword()));
            return ResponseHandler.
                    generateResponse("Berhasil login", HttpStatus.ACCEPTED, data);

        }catch (NoSuchElementException exception){
            return ResponseHandler.
                    generateResponse("user tidak ditemukan!", HttpStatus.NOT_FOUND, data);

        } catch (IllegalArgumentException exception){
            return  ResponseHandler.generateResponse("password yang dimasukkan salah!",
                    HttpStatus.UNAUTHORIZED, data);
        }
    }

    private String getToken(String identifier, String password){
        return jwtProvider.createAuthenticationTokenForUser
                (authService.authenticate(identifier, password).toUserInformationDTO());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> handleCachingRegisterBeforeVerification(@RequestBody  UserRegisterInfoDTO info){
        Map<String, Object> response = new HashMap<>();
        String message = String.format
                ("A confirmation link has been sent to your email address %s. Click the link to verify your account and unlock full access. ", info.getEmail());
        String url = emailAuthenticationProvider.createRegisterURI(info);
        CompletableFuture.runAsync(()->{
            RegisterEmailVerificationDTO emailVerificationDTO =
                    RegisterEmailVerificationDTO.builder()
                            .recipientEmail(info.getEmail())
                            .name(info.getName())
                            .identifier(url)
                            .redirectLink(URI.REGISTRATION_VERIFICATION.getUri()+url).build();
            emailService.sendRegistrationEmailVerification(emailVerificationDTO);
        });
        response.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, response);
    }

    @GetMapping("/register/activate/{link}")
    public ResponseEntity<Object> handleCompleteRegister(@PathVariable String link){
        Map<String, Object> response = new HashMap<>();
        String message = "";
        UserRegisterInfoDTO registerCache = emailAuthenticationProvider.getRegisterCacheFromURI(link);
        if(registerCache == null ){
            message = "The registration verification link is no longer valid.";
            response.put("message", message);
            return  ResponseHandler.generateResponse(message, HttpStatus.UNAUTHORIZED, response);
        }
        else{
            message = String.format("This account under the name of %s has been successfully activated",
                    registerCache.getName());
            response.put("message", message);
            emailAuthenticationProvider.removeRegisterURI(link);
            authService.register(registerCache);
            return  ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, response);
        }
    }
    @PostMapping("/change-password")
    public  ResponseEntity<Object>
    postChangePassword(@RequestBody ChangePasswordInfoDTO info){
        boolean isEmailVerified = emailAuthenticationProvider.isEmailVerified(EmailVerificationType.CHANGE_PASS.getType(),
                info.getIdentifier());
        try{
            if(isEmailVerified){
                authService.changePassword(info.getIdentifier(), info.getPassword(), info.getPasswordConfirmation());
                return ResponseHandler.generateResponse("Berhasil ganti password",
                        HttpStatus.ACCEPTED, new HashMap<>());
            }
            else{
                return  ResponseHandler.generateResponse("Email belum terverifikasi",
                        HttpStatus.UNAUTHORIZED, new HashMap<>());
            }

        }catch (IllegalArgumentException err){
            return  ResponseHandler.generateResponse("Password dan Konfirmasi Password tidak sesuai",
                    HttpStatus.NOT_ACCEPTABLE, new HashMap<>());
        }
    }

    //DONE
    @PostMapping("/email-verification/verify")
    public  CompletableFuture<ResponseEntity<Object>> postVerifyEmailToken(@RequestBody JsonNode requestBody){
        return  CompletableFuture.supplyAsync(()->{
            String email = requestBody.get("email").asText();
            String token = requestBody.get("token").asText();
            Map<String,Object> data = new HashMap<>();
            data.put("isVerified", emailAuthenticationProvider.
                    verifyTokenEmail(requestBody.get("type").asText(),email, token));
            data.put("message", "Server telah memverifikasi token apakah sesuai atau tidak" );
            return  ResponseHandler.generateResponse((String) data.get("message"), HttpStatus.ACCEPTED, data);
        }, virtualExecutor);
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractTokenFromHeader(request.getHeader("Authorization"));
        if(token == null){
            return  ResponseHandler.generateResponse("Tidak ada token authorization",
                    HttpStatus.BAD_REQUEST, new HashMap<>());
        }
        String saved = redisProvider.get(jwtProvider.generateKeyAuthentication(token));
        if(saved == null){
            return ResponseHandler.generateResponse("Token authorization yang ingin " +
                            "dihapus sudah tidak valid",
                    HttpStatus.BAD_REQUEST, new HashMap<>());
        }
        redisProvider.revoke(jwtProvider.generateKeyAuthentication(token));
        return  ResponseHandler.generateResponse("Berhasil logout", HttpStatus.ACCEPTED,
                new HashMap<>());
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}

