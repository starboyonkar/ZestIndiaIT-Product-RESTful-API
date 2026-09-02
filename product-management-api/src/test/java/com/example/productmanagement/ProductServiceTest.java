package com.example.productmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.apache.el.stream.Optional;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.productmanagement.dto.product.ProductRequest;
import com.example.productmanagement.dto.product.ProductResponse;
import com.example.productmanagement.entity.Product;
import com.example.productmanagement.exception.ResourceNotFoundException;
import com.example.productmanagement.repository.ProductRepository;
import com.example.productmanagement.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	private static final java.util.@Nullable Optional<Product> Optional = null;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void shouldCreateProduct() {

		ProductRequest request = ProductRequest.builder().productName("Laptop").build();

		Product saved = Product.builder().id(1L).productName("Laptop").createdBy("onkar").createdOn(LocalDateTime.now())
				.build();

		when(productRepository.save(any(Product.class))).thenReturn(saved);

		ProductResponse response = productService.createProduct(request);

		assertNotNull(response);
		assertEquals(1L, response.getId());
		assertEquals("Laptop", response.getProductName());

		verify(productRepository).save(any(Product.class));
	}

	@Test
	void shouldThrowWhenProductDoesNotExist() {

		when(productRepository.findById(999L)).thenReturn(Optional);

		assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(999L));
	}
}