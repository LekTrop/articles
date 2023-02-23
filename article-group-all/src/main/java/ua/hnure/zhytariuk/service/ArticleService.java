package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import ua.hnure.zhytariuk.models.domain.*;
import ua.hnure.zhytariuk.repo.ArticleRepository;
import ua.hnure.zhytariuk.service.writer.ArticleWriter;
import ua.hnure.zhytariuk.utils.PaginationUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private static final String TAG_DELIMITER = ",";
    public static final String ARTICLE_PATH = "C:\\Users\\iroof\\Downloads\\config-service\\article-group-all\\src\\main\\resources\\articles";

    @NonNull
    private final UserService userService;
    @NonNull
    private final ArticleWriter articleWriter;
    @NonNull
    private ArticleRepository articleRepository;
    @NonNull
    private CategoryService categoryService;
    @NonNull
    private TagService tagService;

    @Transactional(readOnly = true)
    public Page<Article> findAllWithFilters(final String categoryName,
                                            final BigDecimal maxPrice,
                                            final BigDecimal minPrice,
                                            Integer page,
                                            Integer size) {

        page = page == null ? 0 : page;
        size = size == null ? PaginationUtils.DEFAULT_ARTICLE_PAGINATION_SIZE : size;

        return articleRepository.findAllWithFilters(categoryName, maxPrice, minPrice, PageRequest.of(page, size));
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

        final ArticleStatistic articleStatistic = new ArticleStatistic();

        article.addStatistic(articleStatistic);

        final Article createdArticle = articleRepository.save(article);

        articleWriter.createNewFolder(ARTICLE_PATH, createdArticle.getArticleId());

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
