package com.fds.payflow.config;

import com.fds.payflow.utils.AccountFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {
    @Bean
    public AccountFactory accountFactory(){
        return new AccountFactory();
    }
}
