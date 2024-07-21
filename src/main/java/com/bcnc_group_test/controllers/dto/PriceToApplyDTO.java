package com.bcnc_group_test.controllers.dto;

import com.bcnc_group_test.entities.Brand;
import com.bcnc_group_test.entities.CurrencyCode;
import com.bcnc_group_test.entities.Product;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceToApplyDTO {
    @NotNull(message = "Product cannot be null")
    @Positive(message = "Product must be greater than 0")
    private Product product;

    @NotNull(message = "Brand cannot be null")
    @Positive(message = "Brand must be greater than 0")
    private Brand brand;

    @NotNull(message = "Price list cannot be null")
    @Positive(message = "Price list must be greater than 0")
    private Long priceList;

    @NotNull(message = "Start date cannot be null")
    @PastOrPresent
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @PastOrPresent
    private LocalDateTime endDate;

    @NotNull(message = "Price cannot be null")
    @Digits(integer = 10, fraction = 2, message = "Price must be a number")
    private double price;

    @NotNull(message = "Currency cannot be null")
    private CurrencyCode currency;

}
