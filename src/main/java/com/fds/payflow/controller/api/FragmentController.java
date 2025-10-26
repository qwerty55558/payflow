package com.fds.payflow.controller.api;


import com.fds.payflow.constants.PageType;
import com.fds.payflow.constants.SessionConst;
import com.fds.payflow.dto.TransferRequestDto;
import com.fds.payflow.service.AccountService;
import com.fds.payflow.service.FeedService;
import com.fds.payflow.vo.Feed;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/fragments")
public class FragmentController {
    private final AccountService accountService;
    private final TemplateEngine templateEngine;
    private final FeedService feedService;

    @GetMapping("/feed")
    public Map<String, String> getFeedFragment(Model model, HttpSession session) {
        List<Feed> allFeed = feedService.getAllFeed();
        String userId = session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString();
        model.addAttribute("additionalText", PageType.FEED.name());
        model.addAttribute("type", PageType.FEED.name());
        model.addAttribute("userId", userId);
        model.addAttribute("feeds",  allFeed);

        return getFragments(model, "fragments/feed", "feed");
    }

    @GetMapping("/main")
    public Map<String, String> getMainFragment(Model model, HttpSession session) {
        String userId = session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString();
        model.addAttribute("accounts", accountService.findAddressesByMemberUserId(userId));
        model.addAttribute("additionalText", "내 계좌");
        model.addAttribute("type", PageType.MAIN.name());
        model.addAttribute("userId", userId);
        model.addAttribute("transferRequestDto", new TransferRequestDto());
        return getFragments(model, "fragments/main", "main");
    }

    @GetMapping("/more")
    public Map<String, String> getMoreFragment(Model model, HttpSession session) {
        String userId = session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString();
        model.addAttribute("additionalText", PageType.MORE.name());
        model.addAttribute("type", PageType.MORE.name());
        model.addAttribute("userId", userId);
        return getFragments(model, "fragments/more", "more");
    }

    @GetMapping("/product")
    public Map<String, String> getProductFragment(Model model, HttpSession session) {
        String userId = session.getAttribute(SessionConst.LOGIN_MEMBER_NAME.name()).toString();
        model.addAttribute("additionalText", PageType.PRODUCT.name());
        model.addAttribute("type", PageType.PRODUCT.name());
        model.addAttribute("userId", userId);
        return getFragments(model, "fragments/product", "product");
    }

    private String renderFragment(String templatePath, String fragmentSelector, Model model){
        Context context = new Context();
        context.setVariables(model.asMap());
        return templateEngine.process(templatePath, Set.of(fragmentSelector), context);
    }

    private Map<String, String> getFragments(Model model, String templatesPath, String fragmentName) {
        String headerHtml = renderFragment(
                "common/header",
                "header",
                model
        );

        String contentHtml = renderFragment(
                templatesPath,
                fragmentName,
                model
        );

        String footerHtml = renderFragment(
                "common/footer",
                "footer",
                model
        );

        return Map.of("header", headerHtml,
                "content", contentHtml,
                "footer", footerHtml
        );
    }
}
