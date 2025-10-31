package com.fds.payflow.controller;

import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.service.AuthService;
import com.fds.payflow.vo.Member;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager am;

    @GetMapping("/signup")
    public String signupGet(Model model) {
        model.addAttribute("signFormDto");
        return "signup";
    }

    @PostMapping("/login")
    public String loginReq(@Valid LoginFormDto loginFormDto, BindingResult br, HttpSession session) {
        if (br.hasErrors()) {
            log.warn("Login Validation error = {}", br);
            return "welcome";
        }
        log.info("loginFormDto = {}", loginFormDto);
        Member member = authService.

        if (member == null) {
            br.reject(("loginFail"), "존재하지 않는 ID 이거나 비밀번호가 틀립니다.");
            log.warn("Login failed : ID or PW mismatch");
            return "welcome";
        }
    }
}