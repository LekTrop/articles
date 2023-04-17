package ua.hnure.zhytariuk.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.hnure.zhytariuk.controller.validator.ArticleCreationFormValidator;
import ua.hnure.zhytariuk.models.api.ArticleApi;
import ua.hnure.zhytariuk.models.api.SavedArticleApi;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleCreationForm;
import ua.hnure.zhytariuk.models.domain.article.ArticleSearchFilterForm;
import ua.hnure.zhytariuk.models.domain.article.ArticleStatus;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.models.mapper.ArticleMapper;
import ua.hnure.zhytariuk.models.mapper.SavedArticleMapper;
import ua.hnure.zhytariuk.service.CategoryService;
import ua.hnure.zhytariuk.service.UserService;
import ua.hnure.zhytariuk.service.article.ArticleSavedService;
import ua.hnure.zhytariuk.service.article.ArticleService;
import ua.hnure.zhytariuk.service.article.ArticleViewService;

import java.util.*;
import java.util.stream.Collectors;

import static ua.hnure.zhytariuk.utils.PaginationUtils.DEFAULT_ARTICLE_PAGINATION_SIZE;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ArticleController {

    @NonNull
    private final ArticleService articleService;
    @NonNull
    private final CategoryService categoryService;
    @NonNull
    private final ArticleMapper articleMapper;
    @NonNull
    private final ArticleViewService articleViewService;
    @NonNull
    private final UserService userService;

    @GetMapping({"/articles", "/"})
    public String getArticlesPage(
            final Model model,
            final ArticleSearchFilterForm form,
            final Authentication authentication
    ) {
        final List<String> categoryNames = categoryService.findAllCategoryNames();

        final Page<Article> articlePage =
                articleService.findAllWithFilters(
                        form.getUsername(),
                        form.getCategoryName(),
                        form.getTitle(),
                        form.getStartDate(),
                        form.getEndDate(),
                        form.getPage(),
                        DEFAULT_ARTICLE_PAGINATION_SIZE,
                        ArticleStatus.PUBLISHED
                );

        model.addAttribute("articles", articlePage.getContent());;
        model.addAttribute("searchForm", form);
        model.addAttribute("currentPage", articlePage.getNumber());
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("categoryNames", categoryNames);

        return "articles";
    }

    @GetMapping("articles/{articleId}")
    public String getArticlePage(final @PathVariable String articleId,
                                 final Authentication authentication,
                                 final Model model) {
        final Article article = articleService.findById(articleId);

        if (authentication != null) {
            articleViewService.save(articleId, authentication.getName());
        }

        model.addAttribute("article", article);

        return "article";
    }
}
