package com.bcnc_group_test.controllers.dto.mapper;

import com.bcnc_group_test.controllers.dto.BrandDTO;
import com.bcnc_group_test.entities.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public static BrandDTO toBrandDTO(Brand brand) {
        return BrandDTO.builder()
            .name(brand.getName())
            .build();
    }

    public static Brand toBrandEntity(BrandDTO brandDTO) {
        return Brand.builder()
            .name(brandDTO.getName())
            .build();
    }
}
