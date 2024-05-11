package com.isacitra.authentication.common.enums;

import lombok.Getter;

@Getter
public enum EmailVerificationConstants {
    CHANGE_PASS("CHANGE_PASS"),
    GET_TOKEN_REGISTER_BY_EMAIL("GET_TOKEN_REGISTER_BY_EMAIL"),
    REGISTER("REGISTER");

    private final String type;
    EmailVerificationConstants(String type){
        this.type = type;
    }

    public static  boolean contains(String type){
        for( EmailVerificationConstants verificationType : EmailVerificationConstants.values()){
            if(type.equals(verificationType.getType())){
                return  true;
            }
        }
        return  false;
    }
}
