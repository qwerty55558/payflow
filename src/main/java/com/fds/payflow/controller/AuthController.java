package com.fds.payflow.controller;

import com.fds.payflow.constants.SessionConst;
import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.service.AuthService;
import com.fds.payflow.vo.Member;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public String loginReq(@Valid LoginFormDto loginFormDto, BindingResult br, HttpSession session, Model model){
        if (br.hasErrors()) {
            log.warn("Login Validation error = {}", br);
            return "/welcome";
        }
        log.info("loginFormDto = {}", loginFormDto);
        Member member = authService.createMember(loginFormDto.getUserId(), loginFormDto.getPassword());

        if (member == null) {
            br.reject(("loginFail"), "Invalid user ID or password.");
            log.warn("Login filed : ID or PW mismatch");
            return "/welcome";
        }
        session.setAttribute(SessionConst.LOGIN_MEMBER_NAME.name(), member.getUserId());
        session.setAttribute(SessionConst.LOGIN_MEMBER_ID.name(), member.getId());
        return "redirect:/main";
    }
}
