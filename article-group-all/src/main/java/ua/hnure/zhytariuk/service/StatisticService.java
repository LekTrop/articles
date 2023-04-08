package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.service.article.ArticleDislikeService;
import ua.hnure.zhytariuk.service.article.ArticleLikesService;
import ua.hnure.zhytariuk.service.article.ArticleViewService;
import ua.hnure.zhytariuk.service.user.SubscriberService;
import ua.hnure.zhytariuk.utils.DateStatisticApi;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    private final ArticleLikesService articleLikesService;
    private final ArticleDislikeService articleDislikeService;
    private final SubscriberService subscriberService;
    private final ArticleViewService articleViewService;

    private final Map<String, TriFunction<String, Integer, Integer, Map<Integer, Long>>> statisticsByDiagramName;

    public StatisticService(@NonNull ArticleLikesService articleLikesService,
                            @NonNull ArticleDislikeService articleDislikeService,
                            @NonNull SubscriberService subscriberService,
                            @NonNull ArticleViewService articleViewService) {
        this.articleLikesService = articleLikesService;
        this.articleDislikeService = articleDislikeService;
        this.subscriberService = subscriberService;
        this.articleViewService = articleViewService;

        this.statisticsByDiagramName = Map.of(
                "likes", this::getLikesStatistics,
                "dislikes", this::getDislikesStatistics,
                "views", this::getViewsStatistics,
                "subscribers", this::getSubscribersStatistics
        );
    }

    public long[] getStatisticByDiagram(final String username,
                                        final String diagramName,
                                        final DateStatisticApi date) {
        final Map<Integer, Long> statisticsByDayOfMonth = statisticsByDiagramName.get(diagramName)
                                                                                 .apply(username, date.getYear(), date.getMonth());

        long[] data = new long[date.getMonthDays().length];

        for (int i = 0; i < date.getMonthDays().length; i++) {
            if (statisticsByDayOfMonth.containsKey(i + 1)) {
                data[i] = statisticsByDayOfMonth.get(i + 1);
            }else{
                data[i] = 0;
            }
        }

        return data;
    }

    private Map<Integer, Long> getSubscribersStatistics(final String username,
                                                        final Integer year,
                                                        final Integer month) {
        return subscriberService.findAllSubscribersByUsernameAndYearAndMonth(username, month, year)
                                .stream()
                                .collect(Collectors.groupingBy(like -> like.getCreatedAt()
                                                                           .getDayOfMonth(),
                                        Collectors.counting()));
    }

    private Map<Integer, Long> getViewsStatistics(final String username,
                                                  final Integer year,
                                                  final Integer month) {
        return articleViewService.findAllArticleLikesByUsernameAuthorAndMonthAndYear(username, month, year)
                                  .stream()
                                  .collect(Collectors.groupingBy(like -> like.getCreatedAt()
                                                                             .getDayOfMonth(),
                                          Collectors.counting()));
    }

    private Map<Integer, Long> getLikesStatistics(final String username,
                                                  final Integer year,
                                                  final Integer month) {
        return articleLikesService.findAllArticleLikesByUsernameAuthorAndMonthAndYear(username, month, year)
                                  .stream()
                                  .collect(Collectors.groupingBy(like -> like.getCreatedAt()
                                                                             .getDayOfMonth(),
                                          Collectors.counting()));
    }


    private Map<Integer, Long> getDislikesStatistics(final String username,
                                                     final Integer year,
                                                     final Integer month) {
        return articleDislikeService.findAllArticleDislikesByUsernameAuthor(username, month, year)
                                    .stream()
                                    .collect(Collectors.groupingBy(like -> like.getCreatedAt()
                                                                               .getDayOfMonth(),
                                            Collectors.counting()));
    }
}
