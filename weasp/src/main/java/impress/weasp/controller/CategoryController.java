package impress.weasp.controller;

import impress.weasp.controller.dto.categoria.CategoryResponseDto;
import impress.weasp.model.Category;
import impress.weasp.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoriaService categoryService;
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponseDto> response = categories.stream()
                .map(this::mapToCategoryResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private CategoryResponseDto mapToCategoryResponseDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
