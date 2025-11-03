package com.fds.payflow.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.repository.RefreshTokenRepository;
import com.fds.payflow.service.token.TokenProviderService;
import com.fds.payflow.vo.AuthUser;
import com.fds.payflow.vo.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper mapper;
    private final TokenProviderService provider;
    private final RefreshTokenRepository rep;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            LoginFormDto loginFormDto = mapper.readValue(request.getInputStream(), LoginFormDto.class);

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                            loginFormDto.getUserId(),
                            loginFormDto.getPassword(),
                            null
                    );

            return authenticationManager.authenticate(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult instanceof AuthUser user) {
            String AT = provider.genAccessToken(user);
            String RT = provider.genRefreshToken(user);




        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
