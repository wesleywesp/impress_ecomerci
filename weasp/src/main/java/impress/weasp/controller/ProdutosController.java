package impress.weasp.controller;

import impress.weasp.controller.dto.product.ProductResponseDto;
import impress.weasp.model.Category;
import impress.weasp.model.Product;
import impress.weasp.service.CategoriaService;
import impress.weasp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProdutosController {
    private final ProductService productService;
    private final CategoriaService categoryService;

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(@RequestParam(required = false) String query) {
        List<Product> products = productService.searchProducts(query);
        List<ProductResponseDto> response = products.stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);

    }
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> response = products.stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(mapToProductResponseDto(product));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String category) {
        Category categorysub = categoryService.getCategoryBySlug(category);
        List<Product> products = productService.getProductsByCategory(categorysub);
        List<ProductResponseDto> response = products.stream()
                .map(this::mapToProductResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
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
