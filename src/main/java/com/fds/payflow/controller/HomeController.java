package com.fds.payflow.controller;

import com.fds.payflow.constants.PageType;
import com.fds.payflow.constants.SessionConst;
import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.dto.TransferRequestDto;
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
        homeSetting(model, session, accountService);
        return "mainpage";
    }

    public static void homeSetting(Model model, HttpSession session, AccountService service) {
        String userId = session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString();
        model.addAttribute("accounts", service.findAddressesByMemberUserId(userId));
        model.addAttribute("userId", userId);
        model.addAttribute("pageTitle", "내 계좌");
        model.addAttribute("pageType", PageType.MAIN.name());
        model.addAttribute("transferRequestDto", new TransferRequestDto());
    }
}
