package com.fds.payflow.service.token;

import com.fds.payflow.repository.RefreshTokenRepository;
import com.fds.payflow.vo.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository rep;

    public RefreshToken save(RefreshToken refreshToken){
        return rep.save(refreshToken);
    }

    public RefreshToken findByToken(String token){
        return rep.findRefreshTokenByRefreshToken(token).orElseThrow(IllegalStateException::new);
    }
}

