package ua.hnure.zhytariuk.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.hnure.zhytariuk.service.ArticleService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {

    @NonNull
    private final ArticleService articleService;
}
