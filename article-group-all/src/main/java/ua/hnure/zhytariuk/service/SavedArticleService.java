package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.SavedArticle;
import ua.hnure.zhytariuk.repo.SavedArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedArticleService {

    @NonNull
    private final SavedArticleRepository savedArticleRepository;

    public List<SavedArticle> findByUserId(final String userId) {
        return savedArticleRepository.findAllByUserId(userId);
    }
}
