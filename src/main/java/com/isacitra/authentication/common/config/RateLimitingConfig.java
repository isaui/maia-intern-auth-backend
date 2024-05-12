package com.isacitra.authentication.common.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class RateLimitingConfig {

    public static final long LOGIN_LIMIT = 10; // Maximum 10 login attempts
    public static final Duration LOGIN_INTERVAL = Duration.ofMinutes(1); // Per minute

    public static final long REGISTER_LIMIT = 5; // Maximum 5 register attempts
    public static final Duration REGISTER_INTERVAL = Duration.ofMinutes(1); // Per minute

    public static Bucket createLoginBucket() {
        Bandwidth loginBandwidth = Bandwidth.classic(LOGIN_LIMIT, Refill.intervally(LOGIN_LIMIT, LOGIN_INTERVAL));
        return Bucket4j.builder().addLimit(loginBandwidth).build();
    }

    public static Bucket createRegisterBucket() {
        Bandwidth registerBandwidth = Bandwidth.classic(REGISTER_LIMIT, Refill.intervally(REGISTER_LIMIT, REGISTER_INTERVAL));
        return Bucket4j.builder().addLimit(registerBandwidth).build();
    }
}