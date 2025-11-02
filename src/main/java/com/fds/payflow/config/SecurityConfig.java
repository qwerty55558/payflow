package com.fds.payflow.config;

import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.service.MyUserDetailService;
import com.fds.payflow.utils.SimpleEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.security.Provider;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    "/login",
                                    "/signup",
                                    "/",
                                    "/api/**"
                            ).permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> {
                    logout
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true);
                })
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"));
                    ex.accessDeniedPage("/access-denied");
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SimpleEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService service, PasswordEncoder encoder) {
        try {
            DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider(service);
            daoProvider.setPasswordEncoder(encoder);
            ProviderManager providerManager = new ProviderManager(List.of(daoProvider));
            providerManager.setEraseCredentialsAfterAuthentication(false);

            return providerManager;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new MyUserDetailService(memberRepository,encoder);
    }

    @Bean
    public SecurityContextRepository securityContextRepository(){
        return new HttpSessionSecurityContextRepository();
    }
}
