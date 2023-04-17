package ua.hnure.zhytariuk.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.hnure.zhytariuk.models.api.ArticleApi;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Component
public class ArticleCreationFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ArticleApi.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, "title", "Title can`t be empty");
        rejectIfEmptyOrWhitespace(errors, "content", "Content can`t be empty");
        rejectIfEmptyOrWhitespace(errors, "categoryName", "Category name can`t be empty");
    }
}
