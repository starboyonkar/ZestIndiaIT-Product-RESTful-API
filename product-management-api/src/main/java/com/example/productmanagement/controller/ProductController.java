package com.example.productmanagement.controller;

import com.example.productmanagement.dto.product.ProductRequest;
import com.example.productmanagement.dto.product.ProductResponse;
import com.example.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	/*
	 * Create product.
	 */
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {

		ProductResponse response = productService.createProduct(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/*
	 * Get products.
	 */
	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getAllProducts(
			@PageableDefault(size = 10, sort = "createdOn") Pageable pageable) {

		return ResponseEntity.ok(productService.getAllProducts(pageable));
	}

	/*
	 * Get product.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {

		return ResponseEntity.ok(productService.getProductById(id));
	}

	/*
	 * Update product.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductRequest request) {

		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	/*
	 * Delete product.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

		productService.deleteProduct(id);

		return ResponseEntity.noContent().build();
	}
}