package com.fds.payflow.controller;

import com.fds.payflow.constants.SessionConst;
import com.fds.payflow.exceptions.OutOfBalanceException;
import com.fds.payflow.service.MembershipService;
import com.fds.payflow.vo.Membership;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping("/membership/upgrade")
    public String upgradeMembership(
            @RequestParam("membership") String membershipType,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        String userId = session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString();

        try {
            Membership targetMembership = Membership.valueOf(membershipType.toUpperCase());
            membershipService.upgradeMembership(userId, targetMembership);
            redirectAttributes.addFlashAttribute("successMessage", membershipType + " 멤버십으로 업그레이드되었습니다!");
        } catch (OutOfBalanceException e) {
            log.warn("Membership upgrade failed - insufficient balance: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("Membership upgrade failed: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid membership type: {}", membershipType);
            redirectAttributes.addFlashAttribute("errorMessage", "잘못된 멤버십 유형입니다.");
        }

        return "redirect:/main";
    }
}