package com.example.productmanagement;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.productmanagement.repository.ProductRepository;

@SpringBootTest
@ActiveProfiles("test")
class ProductIntegrationTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	void contextLoads() {

		assertNotNull(productRepository);
	}
}