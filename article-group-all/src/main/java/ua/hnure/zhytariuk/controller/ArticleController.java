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
import org.springframework.web.bind.annotation.*;
import ua.hnure.zhytariuk.controller.validator.ArticleCreationFormValidator;
import ua.hnure.zhytariuk.models.api.ArticleApi;
import ua.hnure.zhytariuk.models.api.SavedArticleApi;
import ua.hnure.zhytariuk.models.domain.Article;
import ua.hnure.zhytariuk.models.domain.ArticleCreationForm;
import ua.hnure.zhytariuk.models.domain.ArticleSearchFilterForm;
import ua.hnure.zhytariuk.models.mapper.ArticleMapper;
import ua.hnure.zhytariuk.models.mapper.SavedArticleMapper;
import ua.hnure.zhytariuk.service.ArticleService;
import ua.hnure.zhytariuk.service.CategoryService;
import ua.hnure.zhytariuk.service.SavedArticleService;
import ua.hnure.zhytariuk.utils.PaginationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final ArticleCreationFormValidator articleCreationFormValidator;
    @NonNull
    private final ArticleMapper articleMapper;
    @NonNull
    private final SavedArticleService savedArticleService;
    @NonNull
    private final SavedArticleMapper savedArticleMapper;

    @GetMapping({"/articles", "/"})
    public String getArticlesPage(
            final Model model,
            final ArticleSearchFilterForm form
    ) {
        final List<String> categoryNames = categoryService.findAllCategoryNames();

        final Page<Article> articlePage =
                articleService.findAllWithFilters(form.getCategoryName(),
                        form.getMaxPrice(),
                        form.getMinPrice(),
                        form.getPage(),
                        DEFAULT_ARTICLE_PAGINATION_SIZE
                );

        model.addAttribute("categoryName", form.getCategoryName());
        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("currentPage", articlePage.getNumber());
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("categoryNames", categoryNames);

        return "articles";
    }

    @GetMapping("articles/creation")
    public String getCreateArticlePage(
            final Authentication authentication,
            final Model model) {

        final String username = Optional.ofNullable(authentication)
                                        .map(Authentication::getName)
                                        .orElse(null);

        if (username == null || Objects.equals("anonymousUser", username)) {
            return "redirect:/login";
        }

        model.addAttribute("creationForm", new ArticleCreationForm());
        model.addAttribute("categoryNames", categoryService.findAllCategoryNames());

        return "article-creation";
    }

    @PostMapping("articles/creation")
    public String createArticle(
            final Model model,
            final Authentication authentication,
            final @Validated @ModelAttribute("creationForm") ArticleCreationForm creationForm,
            final BindingResult bindingResult
    ) {

        final String username = Optional.ofNullable(authentication)
                                        .map(Authentication::getName)
                                        .orElse(null);

        if (username == null || Objects.equals("anonymousUser", username)) {
            return "redirect:/login";
        }

        articleCreationFormValidator.validate(creationForm, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryNames", categoryService.findAllCategoryNames());
            return "article-creation";
        }

        articleService.save(
                articleMapper.toDomain(creationForm),
                creationForm.getCategoryName(),
                creationForm.getTags(),
                username
        );

        return "redirect:/";
    }

    @GetMapping("articles/{articleId}")
    public String getArticlePage(final @PathVariable String articleId,
                                 final Model model) {
        final ArticleApi article = articleMapper.toApi(articleService.findById(articleId));

        model.addAttribute("article", article);

        return "article";
    }

    @GetMapping("articles/saved")
    public String getSavedArticlesPage(final Model model) {
        final List<SavedArticleApi> articleApis = savedArticleService.findByUserId("1")
                                                                     .stream()
                                                                     .map(savedArticleMapper::toApi)
                                                                     .collect(Collectors.toList());

        model.addAttribute("savedArticles", articleApis);

        return "saved-articles";
    }
}
