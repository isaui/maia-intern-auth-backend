package com.isacitra.authentication.common.enums;

public enum URI {
    REGISTRATION_VERIFICATION("https://testtestest.com/");
    private final String uri;

    URI(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
    }

