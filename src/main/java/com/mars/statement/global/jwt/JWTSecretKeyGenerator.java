package com.mars.statement.global.jwt;

import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JWTSecretKeyGenerator {

    public static void main(String[] args) {
        // Generate a random 256-bit (32-byte) secret key
        Key secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        // Convert the key to a base64-encoded string
        String base64EncodedSecretKey = io.jsonwebtoken.io.Encoders.BASE64.encode(secretKey.getEncoded());

        System.out.println("JWT Secret Key: " + base64EncodedSecretKey);
    }
}
