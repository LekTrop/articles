package ua.hnure.zhytariuk.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/profile")
@Controller
public class ProfileController {

    @GetMapping
    public String getProfilePage(final Authentication authentication){
//        if(authentication == null){
//            return "redirect:/login";
//        }

        return "profile";
    }
}
