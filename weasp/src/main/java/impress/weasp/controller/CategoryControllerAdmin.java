package impress.weasp.controller;

import impress.weasp.controller.dto.categoria.CategoryRequestDto;
import impress.weasp.controller.dto.categoria.CategoryResponseDto;
import impress.weasp.model.Category;
import impress.weasp.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {

    private final CategoriaService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody @Valid CategoryRequestDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.name())
                .slug(categoryDto.slug())
                .build();
        System.out.println("categoryDto.name() = " + categoryDto.name()+ " categoryDto.slug() = " + categoryDto.slug());
        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToCategoryResponseDto(savedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto categoryDto) {
        Category updatedCategory = Category.builder()
                .name(categoryDto.name())
                .slug(categoryDto.slug())
                .build();
        Category savedCategory = categoryService.updateCategory(id, updatedCategory);
        return ResponseEntity.ok(mapToCategoryResponseDto(savedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

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

