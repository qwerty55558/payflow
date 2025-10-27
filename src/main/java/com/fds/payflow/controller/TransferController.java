package com.fds.payflow.controller;

import com.fds.payflow.dto.TransferRequestDto;
import com.fds.payflow.exceptions.EquityAccountException;
import com.fds.payflow.exceptions.NullAccountException;
import com.fds.payflow.exceptions.OutOfBalanceException;
import com.fds.payflow.service.AccountService;
import com.fds.payflow.vo.Transfer;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TransferController {
    private final AccountService accountService;

    @PostMapping("transfer")
    public String transfer(@Valid @ModelAttribute TransferRequestDto dto, BindingResult br, Model model, HttpSession session){
        if (br.hasErrors()){
            log.warn("Transfer Validation error = {}", br);
            br.reject("globalErrors", "잘못 입력하셨습니다. 다시 입력해주세요.");
            model.addAttribute("globalErrors", br.getGlobalErrors());
            HomeController.homeSetting(model, session, accountService);
            return "mainpage";
        }
        // TODO : 세션 저장 3
//        List<Account> addressesByMemberUserId = accountService.findAddressesByMemberUserId(session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString());
        if (!addressesByMemberUserId.isEmpty()){
            try {
                Transfer transfer = accountService.transferFromAccount(addressesByMemberUserId.getFirst().getAccountNumber(), dto.getAddress(), dto.getAmount());
                if (transfer != null){
                    return "redirect:/main";
                }
            } catch (OutOfBalanceException e) {
                br.reject(HttpStatus.BAD_REQUEST.name(), e.getMessage());
                model.addAttribute("globalErrors", br.getGlobalErrors());
                HomeController.homeSetting(model, session, accountService);
                log.warn("Out of Balance Exception = {}", e.getMessage());
            } catch (NullAccountException e){
                br.reject(HttpStatus.BAD_REQUEST.name(), e.getMessage());
                model.addAttribute("globalErrors", br.getGlobalErrors());
                HomeController.homeSetting(model, session, accountService);
                log.warn("Null Account Exception = {}", e.getMessage());
            } catch (EquityAccountException e){
                br.reject(HttpStatus.BAD_REQUEST.name(), e.getMessage());
                model.addAttribute("globalErrors", br.getGlobalErrors());
                HomeController.homeSetting(model, session, accountService);
                log.warn("Equity Account Exception = {}", e.getMessage());
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        return "mainpage";
    }

}
