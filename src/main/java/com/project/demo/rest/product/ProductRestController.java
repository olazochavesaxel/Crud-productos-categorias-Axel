package com.project.demo.rest.product;

import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductRestController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<Product> createProduct(@PathVariable Long categoryId, @RequestBody Product product) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    product.setCategory(category);
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/category/{categoryId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @PathVariable Long categoryId,
            @RequestBody Product productDetails
    ) {
        return productRepository.findById(id)
                .flatMap(product -> categoryRepository.findById(categoryId)
                        .map(category -> {
                            product.setName(productDetails.getName());
                            product.setDescription(productDetails.getDescription());
                            product.setPrice(productDetails.getPrice());
                            product.setStock(productDetails.getStock());
                            product.setCategory(category);
                            return productRepository.save(product);
                        }))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}