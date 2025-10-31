package com.fds.payflow.service.token;

import com.fds.payflow.config.JwtProperties;
import com.fds.payflow.vo.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
public class TokenProviderService {
    private final JwtProperties properties;
    private final SecretKey secretKey;

    public TokenProviderService(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecretKey().getBytes());
    }

    @Value("${acc-token.duration}")
    private Duration duration;

    public String genToken(Member member) {
        Instant exp = Instant.now().plus(duration);
        return makeToken(Date.from(exp), member);
    }

    public String makeToken(Date expiry, Member user) {
        return Jwts.builder()
                .issuer(properties.getIssuer())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiry.toInstant()))
                .subject(user.getUserId())
                .claim("id", user.getId())
                .signWith(secretKey)
                .compact();
    }

    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> roleUser = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        return new UsernamePasswordAuthenticationToken(
                new User(claims.getSubject(), "", roleUser),
                token,
                roleUser
        );
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
