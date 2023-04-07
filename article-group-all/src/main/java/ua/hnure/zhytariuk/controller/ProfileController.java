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
import ua.hnure.zhytariuk.models.domain.article.ArticleDislike;
import ua.hnure.zhytariuk.models.domain.article.ArticleLike;
import ua.hnure.zhytariuk.service.article.ArticleDislikeService;
import ua.hnure.zhytariuk.service.article.ArticleLikesService;
import ua.hnure.zhytariuk.service.article.ArticleService;
import ua.hnure.zhytariuk.service.article.ArticleViewService;
import ua.hnure.zhytariuk.service.user.SubscriberService;
import ua.hnure.zhytariuk.utils.DateStatisticApi;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static ua.hnure.zhytariuk.utils.StatisticUtils.getMonthDays;

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
                                      "admin",
                                      null,
                                      null,
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
                                          @RequestParam(value = "name", required = false) String diagramName,
                                          @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM") String date,
                                          @RequestParam(value = "articleTitle", required = false) String articleTitle,
                                          final Model model) {

        if (StringUtils.isEmptyOrWhitespace(diagramName)) {
            diagramName = "likes";
        }

        final DateStatisticApi dateStatisticApi = getDateStatistic(date);

        final String username = "admin";
        final List<ArticleLike> articleLikes = articleLikesService.findAllArticleLikesByUsernameAuthor(username, dateStatisticApi.getMonth());
        final List<ArticleDislike> articleDislikes = articleDislikeService.findAllByUsername(username);

        final Map<Integer, Long> map = articleLikes.stream()
                                                   .collect(Collectors.groupingBy(like -> like.getCreatedAt()
                                                                                              .getDayOfMonth(),
                                                           Collectors.counting()));

        final long[] data = new long[dateStatisticApi.getMonthDays().length];

        for (int i = 0; i < dateStatisticApi.getMonthDays().length; i++) {
            if (map.containsKey(i + 1)) {
                data[i] = map.get(i + 1);
            } else {
                data[i] = 0;
            }
        }

        final long totalViews = articleViewService.findTotalViewsByUsername(username);
        final long totalSubscribers = subscriberService.subscribersCountByUsername(username);

        model.addAttribute("date", date);
        model.addAttribute("dateStatistic", dateStatisticApi);
        model.addAttribute("diagramName", diagramName);
        model.addAttribute("data", data);
        model.addAttribute("totalSubscribers", totalSubscribers);
        model.addAttribute("articleLikes", articleLikes);
        model.addAttribute("articleDislikes", articleDislikes);
        model.addAttribute("totalViews", totalViews);

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
