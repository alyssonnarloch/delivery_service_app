package com.app.narlocks.delivery_service_app.extras;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Token {
    public final static String SECRET_KEY_AUTH = "aBH0gO6Ph3lGs4qF0ba5pen55W1f1W9i";
    public final static long TIMEOUT_MS_AUTH = 20000;

    public static String generateForAuthentication(String email, String password) {
        SignatureAlgorithm hs512 = SignatureAlgorithm.HS512;

        long currentTime = System.currentTimeMillis();

        String token = Jwts.builder()
                .claim("email", email)
                .claim("password", password)
                .claim("expireTime", currentTime + TIMEOUT_MS_AUTH)
                .signWith(hs512, SECRET_KEY_AUTH)
                .compact();

        return token;
    }
}
