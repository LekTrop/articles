package ua.hnure.zhytariuk.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class ControllerUtils {

    public static String getAuthNameOrNull(Authentication authentication) {
        return Optional.ofNullable(authentication)
                       .map(Principal::getName)
                       .orElse(null);
    }
}
