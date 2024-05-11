package com.isacitra.authentication.common.enums;

public enum URI {
    REGISTRATION_VERIFICATION("https://maia-authentication-isacitra-fr.up.railway/");
    private final String uri;

    URI(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
    }

