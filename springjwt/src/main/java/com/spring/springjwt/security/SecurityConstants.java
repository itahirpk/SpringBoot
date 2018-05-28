package com.spring.springjwt.security;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(1); //864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
}