package impress.weasp.controller;

import impress.weasp.controller.dto.product.ProductRequestDto;
import impress.weasp.controller.dto.product.ProductResponseDto;
import impress.weasp.model.Category;
import impress.weasp.model.Product;
import impress.weasp.service.CategoriaService;
import impress.weasp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductControllerAdmin {

    private final ProductService productService;
    private final CategoriaService categoryService;



    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto productDto) {
        System.out.println("productDto.categorySlug() = " + productDto.categorySlug());

        Category category = categoryService.getCategoryBySlug(productDto.categorySlug());
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .price(productDto.price())
                .stock(productDto.stock())
                .category(category) // Associação com a categoria
                .imageUrl(productDto.imageUrl())
                .build();
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToProductResponseDto(savedProduct));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto productDto) {

        Category category = categoryService.getCategoryBySlug(productDto.categorySlug());
        Product updatedProduct = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .price(productDto.price())
                .stock(productDto.stock())
                .category(category)
                .imageUrl(productDto.imageUrl())
                .build();
        Product savedProduct = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(mapToProductResponseDto(savedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }



    private ProductResponseDto mapToProductResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getName(), // Nome da categoria
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
