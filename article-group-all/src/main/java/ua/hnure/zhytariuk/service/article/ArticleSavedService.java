package ua.hnure.zhytariuk.service.article;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleSaved;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.article.ArticleSavedRepository;
import ua.hnure.zhytariuk.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleSavedService {

    @NonNull
    private final ArticleSavedRepository savedArticleRepository;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ArticleService articleService;

    public List<ArticleSaved> findAllByUserUsername(final String username) {
        return savedArticleRepository.findAllByUserUsername(username);
    }

    public void saveOrDeleteIfExist(final String articleId, final String username) {
        final Article article = articleService.findById(articleId);
        final User user = userService.loadUserByUsername(username);

        final ArticleSaved articleSaved
                = savedArticleRepository.findByUserUsernameAndArticleArticleId(username, articleId);

        if (articleSaved != null) {
            savedArticleRepository.delete(articleSaved);
        } else {
            savedArticleRepository.save(
                    ArticleSaved.builder()
                                .article(article)
                                .user(user)
                                .build()
            );
        }
    }
}
