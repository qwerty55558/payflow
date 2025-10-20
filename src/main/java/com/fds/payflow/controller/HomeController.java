package com.fds.payflow.controller;

import com.fds.payflow.constants.SessionConst;
import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.service.AccountService;
import com.fds.payflow.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private final AccountService accountService;

    @GetMapping("/" )
    public String home(Model model) {
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "welcome";
    }


    @GetMapping("/main")
    public String login(Model model, HttpSession session) {
        log.info("Login service called");
        String userId = session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString();
        model.addAttribute("accounts", accountService.findAddressesByMemberUserId(userId));
        model.addAttribute("userId", userId);
        return "main";
    }
}
