package com.isacitra.authentication.common.enums;

import lombok.Getter;

@Getter
public enum EmailVerificationType {
    CHANGE_PASS("CHANGE_PASS"),
    REGISTER("REGISTER");

    private final String type;
    EmailVerificationType(String type){
        this.type = type;
    }

    public static  boolean contains(String type){
        for( EmailVerificationType verificationType : EmailVerificationType.values()){
            if(type.equals(verificationType.getType())){
                return  true;
            }
        }
        return  false;
    }
}
