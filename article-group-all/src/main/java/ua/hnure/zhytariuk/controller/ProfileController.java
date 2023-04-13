package ua.hnure.zhytariuk.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import ua.hnure.zhytariuk.controller.validator.ArticleCreationFormValidator;
import ua.hnure.zhytariuk.models.api.SavedArticleApi;
import ua.hnure.zhytariuk.models.domain.UpdateUserRequest;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleCreationForm;
import ua.hnure.zhytariuk.models.domain.article.ArticleSearchFilterForm;
import ua.hnure.zhytariuk.models.domain.article.ArticleStatus;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.models.mapper.ArticleMapper;
import ua.hnure.zhytariuk.models.mapper.SavedArticleMapper;
import ua.hnure.zhytariuk.service.CategoryService;
import ua.hnure.zhytariuk.service.StatisticService;
import ua.hnure.zhytariuk.service.UserService;
import ua.hnure.zhytariuk.service.article.ArticleSavedService;
import ua.hnure.zhytariuk.service.article.ArticleService;
import ua.hnure.zhytariuk.service.article.ArticleViewService;
import ua.hnure.zhytariuk.service.user.SubscriberService;
import ua.hnure.zhytariuk.utils.DateStatisticApi;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static ua.hnure.zhytariuk.controller.ControllerUtils.getAuthNameOrNull;
import static ua.hnure.zhytariuk.utils.StatisticUtils.getMonthDays;

@RequestMapping("/profile")
@Controller
@RequiredArgsConstructor
public class ProfileController {

    @NonNull
    private final StatisticService statisticService;
    @NonNull
    private final ArticleService articleService;
    @NonNull
    private final SubscriberService subscriberService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ArticleSavedService articleSavedService;
    @NonNull
    private final SavedArticleMapper savedArticleMapper;
    @NonNull
    private final CategoryService categoryService;
    @NonNull
    private final ArticleViewService articleViewService;
    @NonNull
    private final ArticleCreationFormValidator articleCreationFormValidator;
    @NonNull
    private final ArticleMapper articleMapper;
    @NonNull
    private final AuthenticationProvider daoAuthenticationProvider;

    @GetMapping
    public String getProfilePage(final Authentication authentication,
                                 final Model model) {
        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

            final int numberOfArticles = articleService.findAllByStatusAndUsername(ArticleStatus.PUBLISHED, username)
                                                       .size();
            final int numberOfFollowers = subscriberService.findAllFollowersByUsername(username)
                                                           .size();

            final int numberOfFollowings = subscriberService.findAllFollowingByUsername(username)
                                                            .size();

            model.addAttribute("numberOfArticles", numberOfArticles);
            model.addAttribute("numberOfSubscribers", numberOfFollowers);
            model.addAttribute("numberOfFollowing", numberOfFollowings);

            model.addAttribute("user", user);

            return "profile";
        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }
    }

    @GetMapping("/users/{username}")
    public String getAuthorProfilePage(final Authentication authentication,
                                       final Model model,
                                       final @PathVariable String username) {

        model.addAttribute("userUsername", "admin");
        model.addAttribute("authorUsername", "user");

        return "author-profile";
    }

    @GetMapping("settings")
    public String getProfileSettingsPage(final Authentication authentication,
                                         final Model model) {
        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

            model.addAttribute("user", user);

            return "profile-settings";
        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }
    }

    @GetMapping("articles")
    public String getProfileArticlesPage(final Authentication authentication,
                                         final ArticleSearchFilterForm form,
                                         final Model model) {
        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }

        final List<Article> articles =
                articleService.findAllWithFilters(
                                      username,
                                      form.getCategoryName(),
                                      form.getTitle(),
                                      form.getStartDate(),
                                      form.getEndDate(),
                                      form.getPage(),
                                      form.getPaginationSize(),
                                      null)
                              .getContent();

        model.addAttribute("articles", articles);
        model.addAttribute("searchForm", form);

        return "profile-articles";
    }

    @GetMapping("statistic")
    public String getProfileStatisticPage(final Authentication authentication,
                                          @RequestParam(value = "name", required = false) String diagramName,
                                          @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM") String date,
                                          @RequestParam(value = "articleTitle", required = false) String articleTitle,
                                          final Model model) {
        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }

        if (StringUtils.isEmptyOrWhitespace(diagramName)) {
            diagramName = "likes";
        }

        final DateStatisticApi dateStatisticApi = getDateStatistic(date);

        final long[] data = statisticService.getStatisticByDiagram(username, diagramName, dateStatisticApi);

        model.addAttribute("date", date);
        model.addAttribute("dateStatistic", dateStatisticApi);
        model.addAttribute("diagramName", diagramName);
        model.addAttribute("statisticData", data);

        return "profile-statistic";
    }

    @GetMapping("articles/saved")
    public String getSavedArticlesPage(final Model model,
                                       final ArticleSearchFilterForm searchFilterForm,
                                       final Authentication authentication) {
        final List<SavedArticleApi> articleApis = articleSavedService.findAllByUserUsername(authentication.getName())
                                                                     .stream()
                                                                     .map(savedArticleMapper::toApi)
                                                                     .collect(Collectors.toList());

        model.addAttribute("searchForm", searchFilterForm);
        model.addAttribute("savedArticles", articleApis);

        return "saved-articles";
    }

    @GetMapping("articles/creation")
    public String getCreateArticlePage(
            final Authentication authentication,
            final Model model) {

        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }

        model.addAttribute("creationForm", new ArticleCreationForm());
        model.addAttribute("categoryNames", categoryService.findAllCategoryNames());

        return "article-creation";
    }

    @GetMapping("edit")
    public String getProfileEditPage(final Authentication authentication,
                                     final Model model) {
        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

            model.addAttribute("user", user);
            model.addAttribute("updateUserRequest", new UpdateUserRequest());

            return "profile-edit";
        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }
    }

    @PostMapping("edit")
    public String editUser(final Authentication authentication,
                           final Model model,
                           final @ModelAttribute("updateUserRequest") UpdateUserRequest updateRequest,
                           final BindingResult bindingResult) {
        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

            final User updatedUser = userService.update(updateRequest, user);

            model.addAttribute("user", updatedUser);

            SecurityContextHolder.getContext()
                                 .setAuthentication(
                                         new UsernamePasswordAuthenticationToken(updatedUser.getUsername(),
                                                 updatedUser.getPassword()));

            return "redirect:/profile";
        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }
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

        return "profile-article";
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

    @GetMapping("articles/{articleId}/edit")
    public String getUpdateArticlePage(
            final Model model,
            final Authentication authentication
    ) {
        final String username = getAuthNameOrNull(authentication);

        try {
            final User user = userService.loadUserByUsername(username);

        } catch (final UsernameNotFoundException ex) {
            return "redirect:/login";
        }

        model.addAttribute("updateForm", new ArticleCreationForm());
        model.addAttribute("categoryNames", categoryService.findAllCategoryNames());

        return "article-update";
    }

    private DateStatisticApi getDateStatistic(String date) {
        if (StringUtils.isEmptyOrWhitespace(date)) {
            final LocalDate localDate = LocalDate.now();

            return DateStatisticApi.builder()
                                   .month(localDate.getMonthValue())
                                   .monthDays(getMonthDays(localDate.getYear(), localDate.getMonthValue()))
                                   .year(localDate.getYear())
                                   .build();
        } else {

            final String[] dates = date.split("-");

            return DateStatisticApi.builder()
                                   .month(Integer.parseInt(dates[1]))
                                   .monthDays(getMonthDays(parseInt(dates[0]), parseInt(dates[1])))
                                   .year(parseInt(dates[0]))
                                   .build();
        }
    }
}
