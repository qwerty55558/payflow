package com.fds.payflow.service;

import com.fds.payflow.exceptions.OutOfBalanceException;
import com.fds.payflow.repository.AccountRepository;
import com.fds.payflow.repository.MemberRepository;
import com.fds.payflow.vo.Account;
import com.fds.payflow.vo.Member;
import com.fds.payflow.vo.Membership;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipService {
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    private static final Long PREMIUM_COST = 5000L;
    private static final Long ELITE_COST = 10000L;

    @Transactional
    public void upgradeMembership(String userId, Membership targetMembership) {
        Member member = memberRepository.findByUserId(userId).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 현재 멤버십 확인
        if (member.getMembership().ordinal() >= targetMembership.ordinal()) {
            throw new IllegalStateException("이미 해당 멤버십이거나 더 높은 등급입니다.");
        }

        // 비용 계산
        Long cost = getCost(targetMembership);

        // 계좌에서 차감
        List<Account> accounts = accountRepository.findAccountsByMemberUserId(userId);
        if (accounts.isEmpty()) {
            throw new IllegalStateException("계좌가 존재하지 않습니다.");
        }

        Account account = accounts.get(0);
        if (account.getBalance() < cost) {
            throw new OutOfBalanceException("멤버십 업그레이드 비용이 부족합니다. 필요 금액: " + cost + "원");
        }

        account.withdraw(cost);
        member.upgradeMembership(targetMembership);

        accountRepository.save(account);
        memberRepository.save(member);

        log.info("User {} upgraded to {} membership", userId, targetMembership);
    }

    private Long getCost(Membership membership) {
        return switch (membership) {
            case PREMIUM -> PREMIUM_COST;
            case ELITE -> ELITE_COST;
            default -> throw new IllegalArgumentException("업그레이드할 수 없는 멤버십입니다.");
        };
    }
}