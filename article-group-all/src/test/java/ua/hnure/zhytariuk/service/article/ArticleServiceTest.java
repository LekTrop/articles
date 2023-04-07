package ua.hnure.zhytariuk.service.article;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.hnure.zhytariuk.models.domain.Category;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleStatus;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.ArticleModerationRepository;
import ua.hnure.zhytariuk.repo.article.ArticleRepository;
import ua.hnure.zhytariuk.service.CategoryService;
import ua.hnure.zhytariuk.service.TagService;
import ua.hnure.zhytariuk.service.UserService;
import ua.hnure.zhytariuk.service.writer.ArticleWriter;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ua.hnure.zhytariuk.models.domain.article.ArticleStatus.DRAFT;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService unit;
    @Mock
    private ArticleRepository mockArticleRepository;
    @Mock
    private UserService userService;
    @Mock
    private ArticleWriter articleWriter;
    @Mock
    private CategoryService categoryService;
    @Mock
    private TagService tagService;
    @Mock
    private ArticleModerationRepository articleModerationRepository;

    @Test
    public void find_by_id_success() {
        //GIVEN
        final Article article = getArticleForTest();

        when(mockArticleRepository.findById(article.getArticleId())).thenReturn(of(article));

        //WHEN
        final Article result = unit.findById(article.getArticleId());

        //THEN
        assertEquals(result, article);

        verify(mockArticleRepository).findById(article.getArticleId());
        verifyNoMoreInteractions(mockArticleRepository);
    }

    @Test
    public void save_article_without_tags_success() {

        //GIVEN
        final Category category = Category.builder()
                                          .categoryId(1L)
                                          .build();
        final Article article = getArticleForTest();
        final User user = User.builder()
                              .id("ADMIN")
                              .build();
        final String categoryName = "CATEGORY";
        final String username = "USERNAME";

        when(categoryService.findByName(categoryName)).thenReturn(Optional.of(category));
        when(userService.loadUserByUsername(username)).thenReturn(user);
        when(mockArticleRepository.save(article)).thenReturn(article);

        final Article result = unit.save(article, categoryName, null, username);

        assertEquals(result, article);
        assertEquals(result.getCategory(), article.getCategory());

        verify(categoryService).findByName(categoryName);
        verify(userService).loadUserByUsername(username);
        verify(mockArticleRepository).save(article);

        verifyNoMoreInteractions(categoryService, userService, mockArticleRepository);
    }


    @Test
    public void findAllWithFilters_success() {
//        final int page = 10;
//        final int size = 5;
//        final String username = "ADMIN";
//        final String categoryName = "CATEGORY_NAME";
//        final ArticleStatus status = DRAFT;
//
//        when(mockArticleRepository.findAllWithFilters(username, categoryName, status, PageRequest.of(page, size)))
//                .thenReturn(new PageImpl<Article>(List.of(getArticleForTest()), PageRequest.of(page, size), 1));
//
//        final Page<Article> articles = unit.findAllWithFilters(username, categoryName, page, size, status);
//
//        assertEquals(getArticleForTest(), articles.getContent()
//                                                  .get(0));
//        assertEquals(page, articles.getPageable()
//                                   .getPageNumber());
//        assertEquals(size, articles.getSize());
//        assertEquals(1, articles.getContent().size());
//
//        verify(mockArticleRepository).findAllWithFilters(username, categoryName, status, PageRequest.of(page, size));
//        verifyNoMoreInteractions(mockArticleRepository);
    }


    private Article getArticleForTest() {
        return Article.builder()
                      .articleId("TEST")
                      .status(ArticleStatus.ON_MODERATION)
                      .build();
    }
}