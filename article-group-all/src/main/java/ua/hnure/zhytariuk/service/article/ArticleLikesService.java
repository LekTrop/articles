package ua.hnure.zhytariuk.service.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleLike;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.article.ArticleLikesRepository;
import ua.hnure.zhytariuk.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleLikesService {
    private final ArticleLikesRepository articleLikesRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public ArticleLike findByUsernameAndArticleId(final String username, final String articleId) {
        return articleLikesRepository.findByUserUsernameAndArticleArticleId(username, articleId);
    }

    public List<ArticleLike> findAllArticleLikesByUsernameAuthorAndMonthAndYear(final String username,
                                                                                final Integer month,
                                                                                final Integer year) {
        return articleLikesRepository.findAllArticleLikesByUsernameAuthorAndYearAndMonth(username, month, year);
    }

    @Transactional
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
            articleLikesRepository.delete(articleLike);
        } else {

            articleLikesRepository.save(ArticleLike.builder()
                                                   .article(article)
                                                   .user(user)
                                                   .build());
        }
    }

    public void deleteById(String articleLikeId) {
        articleLikesRepository.deleteById(articleLikeId);
    }
}
