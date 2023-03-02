package ua.hnure.zhytariuk.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleDislike;
import ua.hnure.zhytariuk.models.domain.article.ArticleLike;
import ua.hnure.zhytariuk.service.article.ArticleDislikeService;
import ua.hnure.zhytariuk.service.article.ArticleLikesService;
import ua.hnure.zhytariuk.service.article.ArticleService;
import ua.hnure.zhytariuk.service.article.ArticleViewService;
import ua.hnure.zhytariuk.service.user.SubscriberService;

import java.util.List;

@RequestMapping("/profile")
@Controller
@RequiredArgsConstructor
public class ProfileController {

    @NonNull
    private final ArticleService articleService;
    @NonNull
    private final ArticleLikesService articleLikesService;
    @NonNull
    private final ArticleDislikeService articleDislikeService;
    @NonNull
    private final ArticleViewService articleViewService;
    @NonNull
    private final SubscriberService subscriberService;

    @GetMapping
    public String getProfilePage(final Authentication authentication) {
        return "profile";
    }

    @GetMapping("/users/{username}")
    public String getAuthorProfilePage(final Authentication authentication,
                                       final Model model,
                                       final @PathVariable String username) {

        model.addAttribute("userUsername", "admin");
        model.addAttribute("authorUsername", "user");

        return "author-profile";
    }

    @GetMapping("articles")
    public String getProfileArticlesPage(final Authentication authentication,
                                         final Model model) {

        final List<Article> articles =
                articleService.findAllWithFilters(
                                      authentication.getName(),
                                      null,
                                      null,
                                      null,
                                      null,
                                      null)
                              .getContent();

        model.addAttribute("articles", articles);

        return "profile-articles";
    }

    @GetMapping("statistic")
    public String getProfileStatisticPage(final Authentication authentication,
                                          final Model model) {

        final String username = authentication.getName();
        final List<ArticleLike> articleLikes = articleLikesService.findAllByUsername(username);
        final List<ArticleDislike> articleDislikes = articleDislikeService.findAllByUsername(username);
        final long totalViews = articleViewService.findTotalViewsByUsername(username);
        final long totalSubscribers = subscriberService.subscribersCountByUsername(username);

        model.addAttribute("totalSubscribers", totalSubscribers);
        model.addAttribute("articleLikes", articleLikes);
        model.addAttribute("articleDislikes", articleDislikes);
        model.addAttribute("totalViews", totalViews);

        return "profile-statistic";
    }
}
