package com.fds.payflow.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    private final AuthService authService;
    private final ResourceLoader loader;
    private final ObjectMapper objectMapper;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            log.info("Init database with JSON data");


            Resource resource = loader.getResource("classpath:/data/member.json");
            InputStream inputStream = resource.getInputStream();

            List<LoginFormDto> list = objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (LoginFormDto loginFormDto : list) {
                authService.createMember(loginFormDto.getUserId(), loginFormDto.getPassword());
            }

            log.info("All members has been saved");
        };

    }
}
