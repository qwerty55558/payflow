package com.fds.payflow.controller;

import com.fds.payflow.dto.LoginFormDto;
import com.fds.payflow.dto.SignFormDto;
import com.fds.payflow.service.AccountService;
import com.fds.payflow.service.AuthService;
import com.fds.payflow.vo.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Security;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final SecurityContextRepository contextRepository;

    @GetMapping("/signup")
    public String signupGet(Model model) {
        model.addAttribute("signFormDto", new SignFormDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String createMember(@Valid SignFormDto signFormDto, BindingResult br, Model model) {
        if (br.hasErrors()) {
            log.warn("Signup Validation Error = {}", br);
            return "signup";
        }
        log.info("signFromDto = {}", signFormDto);

        if (!signFormDto.getPassword().equals(signFormDto.getPasswordConfirm())) {
            br.reject("passwordConfirm", "비밀번호를 다시 확인해주세요.");
            return "signup";
        }

        if (authService.createMember(signFormDto.getUserId(), signFormDto.getPassword()) == null) {
            br.reject("existMember", "이미 존재하는 아이디입니다.");
            return "signup";
        }

        model.addAttribute("loginFormDto", new LoginFormDto());
        return "welcome";
    }

    @PostMapping("/login")
    public String loginReq(@Valid LoginFormDto loginFormDto, BindingResult br, HttpServletRequest req, HttpServletResponse resp) {
        if (br.hasErrors()) {
            log.warn("Login Validation error = {}", br);
            return "welcome";
        }
        log.info("loginFormDto = {}", loginFormDto);

        try {
            Authentication token = UsernamePasswordAuthenticationToken.unauthenticated(
                    loginFormDto.getUserId(),
                    loginFormDto.getPassword()
            );

            Authentication authenticate = authManager.authenticate(token);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticate);
            SecurityContextHolder.setContext(context);
            contextRepository.saveContext(context, req, resp);


            log.info("Login Success: User [{}]", authenticate.getName());
            return "redirect:/main";
        } catch (AuthenticationException e) {
            br.reject("loginFail", "존재하지 않는 ID 이거나 비밀번호가 틀립니다.");
            log.warn("Login failed: {}", e.getMessage());
            return "welcome";
        }
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied";
    }
}