package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.Category;
import ua.hnure.zhytariuk.repo.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @NonNull
    private final CategoryRepository categoryRepository;

    public List<String> findAllCategoryNames() {
        return categoryRepository.findAllCategoryNames();
    }

    public Optional<Category> findByName(final String name) {
        return categoryRepository.findByName(name);
    }
}
