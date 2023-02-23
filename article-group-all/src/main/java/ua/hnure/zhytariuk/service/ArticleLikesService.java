package ua.hnure.zhytariuk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.Article;
import ua.hnure.zhytariuk.models.domain.ArticleLike;
import ua.hnure.zhytariuk.models.domain.User;
import ua.hnure.zhytariuk.repo.ArticleLikesRepository;

@Service
@RequiredArgsConstructor
public class ArticleLikesService {
    private final ArticleLikesRepository articleLikesRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public ArticleLike findByUsernameAndArticleId(final String username, final String articleId) {
        return articleLikesRepository.findByUserUsernameAndArticleArticleId(username, articleId);
    }

    public void save(final String username, final String articleId) {

        final Article article = articleService.findById(articleId);

        if (article == null) {
            throw new RuntimeException("Cannot like because article does not exist");
        }

        final User user = userService.loadUserByUsername(username);

        if (user == null) {
            throw new RuntimeException("Cannot like because user does not exist");
        }

        final ArticleLike articleLike = findByUsernameAndArticleId(username, articleId);

        if (articleLike != null) {
            throw new RuntimeException("Cannot like because like is already done");
        }

        articleLikesRepository.save(ArticleLike.builder()
                                               .article(article)
                                               .user(user)
                                               .build());
    }
}
