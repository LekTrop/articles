package ua.hnure.zhytariuk.service.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleDislike;
import ua.hnure.zhytariuk.models.domain.article.ArticleLike;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.article.ArticleDislikeRepository;
import ua.hnure.zhytariuk.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleDislikeService {
    private final ArticleLikesService articleLikesService;
    private final ArticleDislikeRepository articleDislikeRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public ArticleDislike findByUsernameAndArticleId(final String username, final String articleId) {
        return articleDislikeRepository.findByUserUsernameAndArticleArticleId(username, articleId);
    }

    public List<ArticleDislike> findAllArticleDislikesByUsernameAuthor(final String username, final Integer month, final Integer year) {
        return articleDislikeRepository.findAllUserArticlesDislikesByYearAndMonth(username, year, month);
    }

    @Transactional
    public void dislike(final String username, final String articleId) {

        final Article article = articleService.findById(articleId);

        if (article == null) {
            throw new RuntimeException("Cannot like because article does not exist");
        }

        final User user = userService.loadUserByUsername(username);

        if (user == null) {
            throw new RuntimeException("Cannot like because user does not exist");
        }

        final ArticleLike articleLike = articleLikesService.findByUsernameAndArticleId(username, articleId);

        if (articleLike != null) {
            articleLikesService.deleteById(articleLike.getArticleLikeId());
        }

        final ArticleDislike articleDislike = findByUsernameAndArticleId(username, articleId);

        if (articleDislike != null) {
            articleDislikeRepository.deleteById(articleDislike.getArticleDislikeId());
        } else {
            articleDislikeRepository.save(ArticleDislike.builder()
                                                        .article(article)
                                                        .user(user)
                                                        .build());
        }
    }
}
