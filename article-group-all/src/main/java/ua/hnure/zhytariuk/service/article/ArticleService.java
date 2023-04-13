package ua.hnure.zhytariuk.service.article;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import ua.hnure.zhytariuk.models.domain.Category;
import ua.hnure.zhytariuk.models.domain.Tag;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleModeration;
import ua.hnure.zhytariuk.models.domain.article.ArticleStatus;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.ArticleModerationRepository;
import ua.hnure.zhytariuk.repo.article.ArticleRepository;
import ua.hnure.zhytariuk.service.CategoryService;
import ua.hnure.zhytariuk.service.TagService;
import ua.hnure.zhytariuk.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.hnure.zhytariuk.utils.PaginationUtils.DEFAULT_ARTICLE_PAGINATION_SIZE;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private static final String TAG_DELIMITER = ",";

    @NonNull
    private final UserService userService;
    @NonNull
    private ArticleRepository articleRepository;
    @NonNull
    private CategoryService categoryService;
    @NonNull
    private TagService tagService;
    @NonNull
    private ArticleModerationRepository articleModerationRepository;

    public List<Article> findAllByStatusAndUsername(final ArticleStatus articleStatus,
                                                    final String username){
        return articleRepository.findAllByStatusAndUserUsername(articleStatus, username);
    }

    @Transactional(readOnly = true)
    public Page<Article> findAllWithFilters(
            final String username,
            final String categoryName,
            final String title,
            final LocalDate startDate,
            final LocalDate endDate,
            Integer page,
            Integer size,
            final ArticleStatus status) {

        page = page == null ? 0 : page;
        size = size == null ? DEFAULT_ARTICLE_PAGINATION_SIZE : size;

        return articleRepository.findAllWithFilters(
                username,
                categoryName,
                title,
                startDate,
                endDate,
                status,
                PageRequest.of(page, size));
    }

    @Transactional
    public Article save(final Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public Article save(final Article article,
                        final String categoryName,
                        final String tags,
                        final String username) {
        final Category category = categoryService.findByName(categoryName)
                                                 .orElseThrow();
        article.addCategory(category);

        final User user = userService.loadUserByUsername(username);

        article.addUser(user);

        if (!StringUtils.isEmptyOrWhitespace(tags)) {
            final Set<String> tagNames = Arrays.stream(tags.split(TAG_DELIMITER))
                                               .map(String::trim)
                                               .collect(Collectors.toSet());

            final Set<Tag> existedTags = tagService.getTagsInNames(tagNames);
            final Set<Tag> notExistedTags = getNotExistedTagNamesAndMapToTag(tagNames, existedTags);

            notExistedTags.forEach(tagService::save);
            article.addTags(mergeExistedAndNotExistedTags(existedTags, notExistedTags));
        }

        final Article createdArticle = articleRepository.save(article);

        articleModerationRepository.save(ArticleModeration.builder()
                                                          .article(article)
                                                          .build());

        return createdArticle;
    }

    private Set<Tag> mergeExistedAndNotExistedTags(final Set<Tag> existedTags, final Set<Tag> notExistedTags) {
        return Stream.concat(existedTags.stream(), notExistedTags.stream())
                     .collect(Collectors.toSet());
    }

    private Set<Tag> getNotExistedTagNamesAndMapToTag(final Set<String> tagNames, final Set<Tag> existedTags) {
        return tagNames.stream()
                       .filter(tagName -> existedTags.stream()
                                                     .noneMatch(tag -> Objects.equals(tag.getName(), tagName)))
                       .map(name -> Tag.builder()
                                       .name(name)
                                       .build())
                       .collect(Collectors.toSet());
    }

    public Article findById(final String articleId) {
        return articleRepository.findById(articleId)
                                .orElseThrow();
    }
}
