package com.fds.payflow.service;

import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member findMemberByMemberId(String memberId){
        return memberRepository.findAllByUserId(memberId).getFirst();
    }

    @Transactional(readOnly = true)
    public Member findMemberByMemberIdAndFetching(String memberId){
        return memberRepository.findByUserIdFetching(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

}
