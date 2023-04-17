package ua.hnure.zhytariuk.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.hnure.zhytariuk.controller.validator.ArticleCreationFormValidator;
import ua.hnure.zhytariuk.models.api.ArticleApi;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.mapper.ArticleMapper;
import ua.hnure.zhytariuk.models.mapper.SavedArticleMapper;
import ua.hnure.zhytariuk.service.CategoryService;
import ua.hnure.zhytariuk.service.UserService;
import ua.hnure.zhytariuk.service.article.ArticleSavedService;
import ua.hnure.zhytariuk.service.article.ArticleService;
import ua.hnure.zhytariuk.service.article.ArticleViewService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ArticleController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties"
)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private ArticleCreationFormValidator articleCreationFormValidator;
    @MockBean
    private ArticleMapper articleMapper;
    @MockBean
    private ArticleSavedService articleSavedService;
    @MockBean
    private SavedArticleMapper savedArticleMapper;
    @MockBean
    private ArticleViewService articleViewService;
    @MockBean
    private UserService userService;


    @SneakyThrows
    @Test
    void getArticlePage() {
        final Article article = Article.builder()
                                       .articleId("1")
                                       .build();

        final ArticleApi articleApi = ArticleApi.builder()
                                                .articleId("1")
                                                .build();

        when(articleService.findById("1")).thenReturn(article);
        when(articleMapper.toApi(article)).thenReturn(articleApi);

        mockMvc.perform(get("http://localhost:8084/articles/{articleId}", "1"))
               .andDo(print());

        verify(articleService).findById("1");
        verify(articleMapper).toApi(article);

        verifyNoMoreInteractions(articleService, articleMapper);
    }
}