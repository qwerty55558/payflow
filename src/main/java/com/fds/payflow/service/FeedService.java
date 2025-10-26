package com.fds.payflow.service;

import com.fds.payflow.repository.AccountRepository;
import com.fds.payflow.repository.FeedRepository;
import com.fds.payflow.vo.Feed;
import com.fds.payflow.vo.Member;
import com.fds.payflow.vo.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedService {
    private final FeedRepository repository;
    private final AccountRepository accountRepository;

    public void createFeedAfterTransfer(Transfer created){
        Member sender = accountRepository.findMemberByAccountNumber(created.getFromAccount());
        Member receiver = accountRepository.findMemberByAccountNumber(created.getToAccount());
        String content = sender.getUserId() + " 님이 " + receiver.getUserId() + " 님에게 " + created.getAmount() + " 원을 보냈습니다.";
        repository.save(new Feed("",content,created.getAmount(),"",created.getCreatedAt()));
    }

    @Transactional(readOnly = true)
    public List<Feed> getAllFeed(){
        return repository.findAll();
    }
}
