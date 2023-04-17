package ua.hnure.zhytariuk.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hnure.zhytariuk.service.article.ArticleLikesService;
import ua.hnure.zhytariuk.service.article.ArticleSavedService;

@RestController
@RequestMapping("api/articles/managing/")
@RequiredArgsConstructor
public class ArticleManagingEndpoint {

    @NonNull
    final ArticleLikesService articleLikesService;
    @NonNull
    final ArticleSavedService articleSavedService;

    @PostMapping("/like/{articleId}/{username}")
    public ResponseEntity<?> doLike(final @PathVariable String articleId, final @PathVariable String username) {
        articleLikesService.save(username, articleId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/save/{articleId}/{username}")
    public ResponseEntity<?> doSave(final @PathVariable String articleId, final @PathVariable String username) {
        articleSavedService.saveOrDeleteIfExist(articleId, username);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
