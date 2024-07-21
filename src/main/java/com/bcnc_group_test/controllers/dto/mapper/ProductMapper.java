package com.bcnc_group_test.controllers.dto.mapper;

import com.bcnc_group_test.controllers.dto.ProductDTO;
import com.bcnc_group_test.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductDTO toProductDTO(Product product) {
        return ProductDTO.builder()
            .name(product.getName())
            .build();
    }

    public static Product toProductEntity(ProductDTO productDTO) {
        return Product.builder()
            .name(productDTO.getName())
            .build();
    }
}
