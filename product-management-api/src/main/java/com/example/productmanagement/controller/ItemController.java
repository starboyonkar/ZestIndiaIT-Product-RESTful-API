package com.example.productmanagement.controller;

import com.example.productmanagement.dto.item.ItemCreateRequest;
import com.example.productmanagement.dto.item.ItemResponse;
import com.example.productmanagement.dto.item.ItemUpdateRequest;
import com.example.productmanagement.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	/*
	 * Create an item for a product.
	 *
	 * POST /api/v1/products/{productId}/items
	 */
	@PostMapping
	public ResponseEntity<ItemResponse> createItem(@PathVariable Long productId,
			@Valid @RequestBody ItemCreateRequest request) {

		ItemResponse response = itemService.createItem(productId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Get all items for a product.
	 *
	 * GET /api/v1/products/{productId}/items
	 */
	@GetMapping
	public ResponseEntity<List<ItemResponse>> getItems(@PathVariable Long productId) {

		List<ItemResponse> response = itemService.getItemsByProductId(productId);

		return ResponseEntity.ok(response);
	}

	/*
	 * Get a specific item.
	 *
	 * GET /api/v1/products/{productId}/items/{itemId}
	 */
	@GetMapping("/{itemId}")
	public ResponseEntity<ItemResponse> getItem(@PathVariable Long productId, @PathVariable Long itemId) {

		ItemResponse response = itemService.getItemById(productId, itemId);

		return ResponseEntity.ok(response);
	}

	/*
	 * Update an item.
	 *
	 * PUT /api/v1/products/{productId}/items/{itemId}
	 */
	@PutMapping("/{itemId}")
	public ResponseEntity<ItemResponse> updateItem(@PathVariable Long productId, @PathVariable Long itemId,
			@Valid @RequestBody ItemUpdateRequest request) {

		ItemResponse response = itemService.updateItem(productId, itemId, request);

		return ResponseEntity.ok(response);
	}

	/*
	 * Delete an item.
	 *
	 * DELETE /api/v1/products/{productId}/items/{itemId}
	 */
	@DeleteMapping("/{itemId}")
	public ResponseEntity<Void> deleteItem(@PathVariable Long productId, @PathVariable Long itemId) {

		itemService.deleteItem(productId, itemId);

		return ResponseEntity.noContent().build();
	}
}