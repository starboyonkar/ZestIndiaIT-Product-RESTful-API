package com.example.productmanagement.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

	@NotBlank(message = "Product name is required")
	@Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
	private String productName;
}