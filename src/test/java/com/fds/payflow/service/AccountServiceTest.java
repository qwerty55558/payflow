package com.fds.payflow.service;

import com.fds.payflow.vo.Account;
import com.fds.payflow.vo.Feed;
import com.fds.payflow.vo.Member;
import com.fds.payflow.vo.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class AccountServiceTest {
    @Autowired
    AccountService accountService;

    @Autowired
    MemberService memberService;

    @Autowired
    FeedService feedService;

    @DisplayName("계좌 이체 테스트 - success")
    @Test
    void transferTest(){
        Transfer transfer = transferTestTemplate(50000L);
        assertNotNull(transfer);
    }

    @DisplayName("계좌 이체 테스트 - fail (잔액 부족)")
    @Test
    void transferFailTest(){
        Transfer transfer = transferTestTemplate(100001L);
        assertNull(transfer);
    }

    @DisplayName("피드 생성 테스트 - success")
    @Test
    void transferWithFeedCreate(){
        transferTestTemplate(10000L);
        List<Feed> allFeed = feedService.getAllFeed();
        assertNotNull(allFeed);
        for (Feed feed : allFeed) {
            log.info("feed = {}", feed.toString());
        }
    }

    private Transfer transferTestTemplate(Long amount){
        Member user001 = memberService.findMemberByMemberId("user001");
        Member user002 = memberService.findMemberByMemberId("user002");

        Account accountFrom = accountService.findAddressesByMemberUserId(user001.getUserId()).stream().findFirst().orElseThrow();
        Account accountTo = accountService.findAddressesByMemberUserId(user002.getUserId()).stream().findFirst().orElseThrow();


        Transfer transfer = accountService.transferFromAccount(accountFrom.getAccountNumber(), accountTo.getAccountNumber(), amount);
        log.info("transfer = {}", transfer);

        Member user1after = memberService.findMemberByMemberIdAndFetching("user001");
        Member user2after = memberService.findMemberByMemberIdAndFetching("user002");
        log.info("user1after account balance = {}", user1after.getAccounts().stream().findFirst().orElseThrow().getBalance());
        log.info("user2after account balance = {}", user2after.getAccounts().stream().findFirst().orElseThrow().getBalance());

        return transfer;
    }
}