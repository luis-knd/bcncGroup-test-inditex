package com.bcnc_group_test.controllers.dto.mapper;

import com.bcnc_group_test.controllers.dto.PriceDTO;
import com.bcnc_group_test.controllers.dto.PriceToApplyDTO;
import com.bcnc_group_test.entities.Price;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PriceMapper {

    private static final DateTimeFormatter INPUT_FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter OUTPUT_FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");


    public static PriceDTO toPriceDTO(Price price) {
        String formattedStartDate = price.getStartDate().format(OUTPUT_FORMATTER_DATE);
        String formattedEndDate = price.getEndDate().format(OUTPUT_FORMATTER_DATE);
        return PriceDTO.builder()
            .product(price.getProduct())
            .brand(price.getBrand())
            .price(price.getPrice())
            .currency(price.getCurrency())
            .startDate(LocalDateTime.parse(formattedStartDate, OUTPUT_FORMATTER_DATE))
            .endDate(LocalDateTime.parse(formattedEndDate, OUTPUT_FORMATTER_DATE))
            .priceList(price.getPriceList())
            .priority(price.getPriority())
            .build();
    }

    public static Price toPriceEntity(PriceDTO priceDTO) {
        LocalDateTime startDate = LocalDateTime.parse(priceDTO.getStartDate().format(INPUT_FORMATTER_DATE), INPUT_FORMATTER_DATE);
        LocalDateTime endDate = LocalDateTime.parse(priceDTO.getEndDate().format(INPUT_FORMATTER_DATE), INPUT_FORMATTER_DATE);

        return Price.builder()
            .product(priceDTO.getProduct())
            .brand(priceDTO.getBrand())
            .price(priceDTO.getPrice())
            .currency(priceDTO.getCurrency())
            .startDate(startDate)
            .endDate(endDate)
            .priceList(priceDTO.getPriceList())
            .priority(priceDTO.getPriority())
            .build();
    }

    public static PriceToApplyDTO toPriceToApplyDTO(Price price) {
        String formattedStartDate = price.getStartDate().format(OUTPUT_FORMATTER_DATE);
        String formattedEndDate = price.getEndDate().format(OUTPUT_FORMATTER_DATE);
        return PriceToApplyDTO.builder()
            .product(price.getProduct())
            .brand(price.getBrand())
            .priceList(price.getPriceList())
            .startDate(LocalDateTime.parse(formattedStartDate, OUTPUT_FORMATTER_DATE))
            .endDate(LocalDateTime.parse(formattedEndDate, OUTPUT_FORMATTER_DATE))
            .price(price.getPrice())
            .currency(price.getCurrency())
            .build();
    }

}
