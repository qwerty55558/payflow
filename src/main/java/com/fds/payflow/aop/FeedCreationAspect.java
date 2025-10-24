package com.fds.payflow.aop;

import com.fds.payflow.service.FeedService;
import com.fds.payflow.vo.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class FeedCreationAspect {
    private final FeedService feedService;

    @Pointcut("execution(* com.fds.payflow.service.AccountService.transferFromAccount(..))")
    public void createTransferPointcut(){}

    @AfterReturning(pointcut = "createTransferPointcut()", returning = "created")
    public void createFeedAfterTransfer(JoinPoint jp, Transfer created) {
        if (created != null) {
            log.info("AOP: Transfer created id ={}",created.getFromAccount());
            feedService.createFeedAfterTransfer(created);
        }
    }
}
