package com.security.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    public static final String SECRET = "MySuperSecretKey12345#098Sushil&Jyoti";

    public String generateToken(String username){
        LOGGER.info("JwtService:: generateToken: Inside it.");
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60))
                .claims(new HashMap<>())
                //.signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .signWith(getSignedKey())
                .compact();
    }

    private Key getSignedKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public Claims verifySignatureAndExtractClaims(String token){
        /*return Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();*/
        return Jwts.parser()
                .verifyWith((SecretKey) getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserName(String token){
        return verifySignatureAndExtractClaims(token)
                .getSubject();
    }

    public Date getExpiration(String token){
        return verifySignatureAndExtractClaims(token)
                .getExpiration();
    }

    public boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

}
