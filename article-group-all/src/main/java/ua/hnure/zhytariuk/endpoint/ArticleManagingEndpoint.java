package ua.hnure.zhytariuk.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hnure.zhytariuk.service.ArticleLikesService;

@RestController
@RequestMapping("api/articles/managing/")
@RequiredArgsConstructor
public class ArticleManagingEndpoint {

    @NonNull
    final ArticleLikesService articleLikesService;

    @PostMapping("/like")
    public ResponseEntity<?> doLike(final String articleId, final String username) {
        articleLikesService.save(username, articleId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
