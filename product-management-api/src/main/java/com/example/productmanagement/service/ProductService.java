package com.example.productmanagement.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagement.dto.product.ProductRequest;
import com.example.productmanagement.dto.product.ProductResponse;
import com.example.productmanagement.entity.Product;
import com.example.productmanagement.exception.ResourceNotFoundException;
import com.example.productmanagement.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	/*
	 * Create product.
	 */
	public ProductResponse createProduct(ProductRequest request) {

		String username = getCurrentUsername();

		LocalDateTime now = LocalDateTime.now();

		Product product = Product.builder().productName(request.getProductName().trim()).createdBy(username)
				.createdOn(now).build();

		Product savedProduct = productRepository.save(product);

		return mapToResponse(savedProduct);
	}

	/*
	 * Get all products with pagination.
	 */
	@Transactional(readOnly = true)
	public Page<ProductResponse> getAllProducts(Pageable pageable) {

		return productRepository.findAll(pageable).map(this::mapToResponse);
	}

	/*
	 * Get product by ID.
	 */
	@Transactional(readOnly = true)
	public ProductResponse getProductById(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		return mapToResponse(product);
	}

	/*
	 * Update product.
	 */
	public ProductResponse updateProduct(Long id, ProductRequest request) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		product.setProductName(request.getProductName().trim());

		product.setModifiedBy(getCurrentUsername());

		product.setModifiedOn(LocalDateTime.now());

		Product updatedProduct = productRepository.save(product);

		return mapToResponse(updatedProduct);
	}

	/*
	 * Delete product.
	 */
	public void deleteProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		productRepository.delete(product);
	}

	/*
	 * Get currently authenticated username.
	 */
	private String getCurrentUsername() {

		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	/*
	 * Entity → Response DTO.
	 */
	private ProductResponse mapToResponse(Product product) {

		return ProductResponse.builder().id(product.getId()).productName(product.getProductName())
				.createdBy(product.getCreatedBy()).createdOn(product.getCreatedOn()).modifiedBy(product.getModifiedBy())
				.modifiedOn(product.getModifiedOn()).build();
	}
}