package com.fds.payflow.service;

import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findMemberByMemberId(String memberId){
        return memberRepository.findByUserId(memberId).getFirst();
    }

    public Member findMemberByMemberIdAndFetching(String memberId){
        return memberRepository.findByUserIdFetching(memberId);
    }

}
