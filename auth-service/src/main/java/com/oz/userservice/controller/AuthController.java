package com.oz.userservice.controller;

import com.oz.userservice.models.api.UserApi;
import com.oz.userservice.models.mapper.UserMapper;
import com.oz.userservice.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    @NonNull
    private final UserService userDetailsService;
    @NonNull
    private final UserMapper userMapper;

    @GetMapping("/registration")
    public String getRegistrationPage(final Model model) {
        model.addAttribute("registrationForm", new UserApi());

        return "registration";
    }

    @PostMapping("/registration")
    public String doUserRegistration(final @ModelAttribute("registrationForm") UserApi registeredUser) {
        userDetailsService.save(userMapper.toDomain(registeredUser));

        return "redirect:/login";
    }
}
