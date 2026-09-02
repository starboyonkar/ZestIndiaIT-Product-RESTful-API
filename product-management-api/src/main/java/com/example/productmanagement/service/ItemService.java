package com.example.productmanagement.service;

import com.example.productmanagement.dto.item.ItemCreateRequest;
import com.example.productmanagement.dto.item.ItemResponse;
import com.example.productmanagement.dto.item.ItemUpdateRequest;
import com.example.productmanagement.entity.Item;
import com.example.productmanagement.entity.Product;
import com.example.productmanagement.exception.ResourceNotFoundException;
import com.example.productmanagement.repository.ItemRepository;
import com.example.productmanagement.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

	private final ItemRepository itemRepository;
	private final ProductRepository productRepository;

	/*
	 * Create an item for a product.
	 */
	public ItemResponse createItem(Long productId, ItemCreateRequest request) {

		Product product = findProductById(productId);

		Item item = Item.builder().product(product).quantity(request.getQuantity()).build();

		Item savedItem = itemRepository.save(item);

		return mapToResponse(savedItem);
	}

	/*
	 * Get all items belonging to a product.
	 */
	@Transactional(readOnly = true)
	public List<ItemResponse> getItemsByProductId(Long productId) {

		// Make sure the product exists.
		findProductById(productId);

		return itemRepository.findByProductId(productId).stream().map(this::mapToResponse).toList();
	}

	/*
	 * Get a specific item belonging to a product.
	 */
	@Transactional(readOnly = true)
	public ItemResponse getItemById(Long productId, Long itemId) {

		Item item = findItemByProductId(productId, itemId);

		return mapToResponse(item);
	}

	/*
	 * Update an item belonging to a product.
	 */
	public ItemResponse updateItem(Long productId, Long itemId, ItemUpdateRequest request) {

		Item item = findItemByProductId(productId, itemId);

		item.setQuantity(request.getQuantity());

		Item updatedItem = itemRepository.save(item);

		return mapToResponse(updatedItem);
	}

	/*
	 * Delete an item belonging to a product.
	 */
	public void deleteItem(Long productId, Long itemId) {

		Item item = findItemByProductId(productId, itemId);

		itemRepository.delete(item);
	}

	/*
	 * Find product or throw 404.
	 */
	private Product findProductById(Long productId) {

		return productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
	}

	/*
	 * Find item belonging specifically to a product.
	 */
	private Item findItemByProductId(Long productId, Long itemId) {

		// First verify product exists.
		findProductById(productId);

		return itemRepository.findByIdAndProductId(itemId, productId).orElseThrow(
				() -> new ResourceNotFoundException("Item with id " + itemId + " not found for product " + productId));
	}

	/*
	 * Entity → DTO.
	 */
	private ItemResponse mapToResponse(Item item) {

		return ItemResponse.builder().id(item.getId()).productId(item.getProduct().getId()).quantity(item.getQuantity())
				.build();
	}
}