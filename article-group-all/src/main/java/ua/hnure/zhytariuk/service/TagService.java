package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnure.zhytariuk.models.domain.Tag;
import ua.hnure.zhytariuk.repo.TagRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {
    @NonNull
    private final TagRepository tagRepository;

    public Set<Tag> getTagsInNames(final Set<String> names) {
        return tagRepository.getTagsInNames(names);
    }

    @Transactional
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }
}
