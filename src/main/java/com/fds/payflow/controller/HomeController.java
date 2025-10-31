package com.fds.payflow.controller;

import com.fds.payflow.constants.PageType;
import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.dto.TransferRequestDto;
import com.fds.payflow.service.AccountService;
import com.fds.payflow.service.MemberService;
import com.fds.payflow.vo.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

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
    public String login(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Login service called");
        homeSetting(model, accountService,userDetails.getUsername());
        return "mainpage";
    }

    public static void homeSetting(Model model, AccountService service, String username) {
        model.addAttribute("accounts", service.findAddressesByMemberUserId(username));
        model.addAttribute("userId", username);
        model.addAttribute("pageTitle", "내 계좌");
        model.addAttribute("pageType", PageType.MAIN.name());
        model.addAttribute("transferRequestDto", new TransferRequestDto());
    }
}
