package com.isacitra.authentication.common.enums;

import lombok.Getter;

@Getter
public enum RedisConstants {
    AUTH_TOKEN("authentication::jwt::"),
    VERIFICATION_CHANGE_PASSWORD("verification::change-password::"),
    TOKEN_REGISTER_EMAIL("verification::token::"),
    VERIFICATION_REGISTER_EMAIL("verification::email::register::");
    private final String prefix;
    RedisConstants(String prefix) {
        this.prefix = prefix;
    }
}

