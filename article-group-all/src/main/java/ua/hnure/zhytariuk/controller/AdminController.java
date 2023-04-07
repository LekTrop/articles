package ua.hnure.zhytariuk.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.hnure.zhytariuk.models.domain.article.*;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.ArticleModerationRepository;
import ua.hnure.zhytariuk.service.CategoryService;
import ua.hnure.zhytariuk.service.UserService;
import ua.hnure.zhytariuk.service.article.ArticleService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @NonNull
    private ArticleService articleService;
    @NonNull
    private CategoryService categoryService;
    @NonNull
    private ArticleModerationRepository articleModerationRepository;
    @NonNull
    private UserService userService;

    @GetMapping({"/admin-panel", "admin-panel/moderation"})
    public String getAdminPanelPage(
            final Model model,
            final Authentication authentication,
            final ArticleSearchFilterForm form) {

        final List<String> categoryNames = categoryService.findAllCategoryNames();

        final Page<Article> articlePage = articleService.findAllWithFilters(
                form.getUsername(),
                form.getCategoryName(),
                form.getTitle(),
                form.getStartDate(),
                form.getEndDate(),
                form.getPage(),
                form.getPaginationSize(),
                ArticleStatus.ON_MODERATION
        );

        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("categoryName", form.getCategoryName());
        model.addAttribute("currentPage", articlePage.getNumber());
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("categoryNames", categoryNames);

        return "admin-panel";
    }

    @GetMapping("/admin-panel/moderation/{articleId}")
    public String getAdminArticleModerationPanelPage(
            final Model model,
            final @PathVariable String articleId) {

        final Article article = articleService.findById(articleId);
        final ArticleModeration articleModeration = articleModerationRepository.findById(articleId)
                                                                               .get();
        model.addAttribute("article", article);
        model.addAttribute("articleModeration", articleModeration);

        return "article-moderation-page";
    }

    @PostMapping("/admin-panel/moderation/{articleId}")
    public String updateModerationStatus(
            final @ModelAttribute(name = "articleModeration") ArticleModeration newModeration,
            final Authentication authentication,
            final @PathVariable String articleId
    ) {
        final ArticleModeration existed = articleModerationRepository.findById(articleId)
                                                                     .orElse(null);
        final User user = userService.loadUserByUsername("admin");

        if (newModeration != null && existed != null) {
            if (newModeration.getStatus() != existed.getStatus()) {
                final Article article = articleService.findById(articleId);

                if (newModeration.getStatus() == ModerationStatus.APPROVED) {
                    articleModerationRepository.save(existed.toBuilder()
                                                            .status(newModeration.getStatus())
                                                            .additionalInformation(newModeration.getAdditionalInformation())
                                                            .article(article.toBuilder()
                                                                            .status(ArticleStatus.PUBLISHED)
                                                                            .user(user)
                                                                            .build())
                                                            .build());
                } else if (newModeration.getStatus() == ModerationStatus.CANCELED) {
                    articleModerationRepository.save(existed.toBuilder()
                                                            .status(newModeration.getStatus())
                                                            .additionalInformation(newModeration.getAdditionalInformation())
                                                            .article(article.toBuilder()
                                                                            .status(ArticleStatus.CANCELED_MODERATION)
                                                                            .user(user)
                                                                            .build())
                                                            .build());
                } else {
                    articleModerationRepository.save(existed.toBuilder()
                                                            .additionalInformation(newModeration.getAdditionalInformation())
                                                            .status(newModeration.getStatus())
                                                            .build());
                }
            }
        }


        return "redirect:/admin-panel";
    }
}
