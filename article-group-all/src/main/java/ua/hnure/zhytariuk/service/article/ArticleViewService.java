package ua.hnure.zhytariuk.service.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleView;
import ua.hnure.zhytariuk.repo.article.ArticleViewRepository;
import ua.hnure.zhytariuk.service.UserService;

@RequiredArgsConstructor
@Service
public class ArticleViewService {
    private final ArticleService articleService;
    private final UserService userService;
    private final ArticleViewRepository articleViewRepository;

    public void save(final String articleId, final String username) {
        final Article article = articleService.findById(articleId);

        if (article == null) {
            throw new RuntimeException();
        }

        final User user = userService.loadUserByUsername(username);

        if (user == null) {
            throw new RuntimeException();
        }

        final ArticleView articleView = articleViewRepository.findByUserUsernameAndArticleArticleId(username, articleId);

        if (articleView == null) {
            articleViewRepository.save(ArticleView.builder()
                                                  .article(article)
                                                  .user(user)
                                                  .build());
        }
    }

    public long findTotalViewsByUsername(final String name) {
        return articleViewRepository.countByUserUsername(name);
    }
}
