package com.fds.payflow.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Arrays;

@Slf4j
public class SimpleEncoder implements PasswordEncoder {
    @Value(value = "${security.hashing.salt_length}")
    private Integer saltLength;

    @Value(value = "${security.hashing.cost_factor}")
    private Integer costFactor;


    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try{
            return OpenBSDBCrypt.checkPassword(encodedPassword, rawPassword.toString().getBytes());
        } catch (Exception e){
            log.info("Password matching error: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // salt setup
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[saltLength];
        secureRandom.nextBytes(bytes);

        // bCrypt encoding
        return OpenBSDBCrypt.generate(rawPassword.toString().getBytes(), bytes, costFactor);
    }
}
