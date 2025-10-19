package com.fds.payflow.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Slf4j
public class SimpleEncoder {
    @Value(value = "${security.hashing.salt_length}")
    private String saltLength;

    @Value(value = "${security.hashing.cost_factor}")
    private String costFactor;

    public String encode(String rawPassword){
        // salt setup
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[Integer.parseInt(saltLength)];
        secureRandom.nextBytes(bytes);

        // bCrypt encoding
        return OpenBSDBCrypt.generate(rawPassword.toCharArray(), bytes, Integer.parseInt(costFactor));
    }

    public boolean matches(String rawPassword, String storedHash){
        try {
            return OpenBSDBCrypt.checkPassword(storedHash, rawPassword.toCharArray());
        } catch (Exception e) {
            log.info("Password matching error: {}", e.getMessage());
            return false;
        }
    }
}
