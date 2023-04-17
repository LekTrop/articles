package ua.hnure.zhytariuk.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.hnure.zhytariuk.models.api.UserApi;
import ua.hnure.zhytariuk.models.mapper.UserMapper;
import ua.hnure.zhytariuk.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {
    @NonNull
    private final UserService userDetailsService;
    @NonNull
    private final UserMapper userMapper;

    @GetMapping({"login", "registration"})
    public String getAuthPage(final Model model) {

        model.addAttribute("loginForm", new UserApi());
        model.addAttribute("registrationForm", new UserApi());

        return "login";
    }

    @PostMapping("/login")
    public String doLogin(final @Validated @ModelAttribute("loginForm") UserApi loginForm) {

        return "redirect:/";
    }

    @PostMapping("/registration")
    public String doUserRegistration(final @ModelAttribute("registrationForm") UserApi registeredUser) {
        userDetailsService.save(userMapper.toDomain(registeredUser));

        return "redirect:/login";
    }
}
