package com.fds.payflow.service.token;

import com.fds.payflow.config.JwtProperties;
import com.fds.payflow.vo.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenProviderService {
    private final JwtProperties properties;
    private final SecretKey secretKey;

    public TokenProviderService(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecretKey().getBytes());
    }

    @Value("${acc-token.duration}")
    private Duration atDuration;

    @Value("${ref-token.duration}")
    private Duration rtDuration;

    public String genRefreshToken(AuthUser user){
        Instant exp = Instant.now().plus(rtDuration);
        return makeToken(Date.from(exp), user, "RT");
    }

    public String genAccessToken(AuthUser user) {
        Instant exp = Instant.now().plus(atDuration);
        return makeToken(Date.from(exp), user, "AT");
    }

    public String makeToken(Date expiry, AuthUser user, String tokenType) {
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .issuer(properties.getIssuer())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiry.toInstant()))
                .subject(user.getUsername())
                .claim("id", user.getUserId())
                .claim("type", tokenType)
                .claim("auth", authorities)
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

        if (!"AT".equals(claims.get("type", String.class))) {
            throw new JwtException("Invalid token Type: 토큰에 액세스할 수가 없습니다.");
        }

        String authClaims = claims.get("auth", String.class);

        Collection<? extends GrantedAuthority> authorities = extractAuth(authClaims);

        return new UsernamePasswordAuthenticationToken(
                new AuthUser(claims.getSubject(), "", true, true, true, true, authorities, Long.parseLong(claims.getId())),
                token,
                authorities
        );
    }

    private static Collection<? extends GrantedAuthority> extractAuth(String authClaims) {
        return (authClaims == null || authClaims.isBlank()) ? Collections.emptyList() :
        Arrays.stream(authClaims.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getATFromRT(String refreshToken){
        if (!validToken(refreshToken)){
            throw new JwtException("Invalid Token: 토큰이 유효하지 않습니다.");
        }
        Claims claims = getClaims(refreshToken);
        if (!"RT".equals(claims.get("type", String.class))) {
            throw new JwtException("Invalid token Type: 토큰에 액세스할 수가 없습니다.");
        }

        String userName = claims.getSubject();
        Long userId = claims.get("id", Long.class);
        String auth = claims.get("auth", String.class);

        AuthUser authUser = new AuthUser(userName, "", true, true, true, true, extractAuth(auth), userId);
        return genAccessToken(authUser);
    }

}
