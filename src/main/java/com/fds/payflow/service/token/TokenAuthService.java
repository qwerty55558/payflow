package com.fds.payflow.service.token;

import com.fds.payflow.repository.RefreshTokenRepository;
import com.fds.payflow.service.MemberService;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenAuthService {
    private final TokenProviderService provider;
    private final RefreshTokenService refService;
    private final MemberService mem;

    public String createNewAccessToken(String refreshToken){
        if (!provider.validToken(refreshToken)){
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        Long userId = refService.findByToken(refreshToken).getUserId();

        Member member = mem.findMemberById(userId).orElseThrow(IllegalStateException::new);

        return provider.genToken(member);
    }
}
