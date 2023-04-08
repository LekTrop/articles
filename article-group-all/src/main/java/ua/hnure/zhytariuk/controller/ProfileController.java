package ua.hnure.zhytariuk.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleSearchFilterForm;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.service.StatisticService;
import ua.hnure.zhytariuk.service.UserService;
import ua.hnure.zhytariuk.service.article.ArticleService;
import ua.hnure.zhytariuk.utils.DateStatisticApi;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
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
    private final UserService userService;

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

    @GetMapping("articles/settings")
    public String getProfileSettingsPage(final Authentication authentication,
                                         final Model model) {
        final String username = Optional.ofNullable(authentication)
                                        .map(Principal::getName)
                                        .orElse(null);

        final User user = userService.loadUserByUsername(username);

        model.addAttribute("user", user);

        return "profile-settings";
    }

    @GetMapping("articles")
    public String getProfileArticlesPage(final Authentication authentication,
                                         final ArticleSearchFilterForm form,
                                         final Model model) {

        final List<Article> articles =
                articleService.findAllWithFilters(
                                      authentication.getName(),
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

        if (StringUtils.isEmptyOrWhitespace(diagramName)) {
            diagramName = "likes";
        }

        final DateStatisticApi dateStatisticApi = getDateStatistic(date);

        final String username = "admin";

        final long[] data = statisticService.getStatisticByDiagram(username, diagramName, dateStatisticApi);

        model.addAttribute("date", date);
        model.addAttribute("dateStatistic", dateStatisticApi);
        model.addAttribute("diagramName", diagramName);
        model.addAttribute("statisticData", data);

        return "profile-statistic";
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
