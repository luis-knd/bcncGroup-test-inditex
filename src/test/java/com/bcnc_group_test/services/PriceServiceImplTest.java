package com.bcnc_group_test.services;

import com.bcnc_group_test.entities.Price;
import com.bcnc_group_test.services.impl.PriceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PriceServiceImplTest {

    @Autowired
    private PriceServiceImpl priceService;
    private long productId;
    private long brandId;

    @BeforeEach
    public void setUp() {
        productId = 35455L;
        brandId = 1L;
    }

    @Test
    public void getAllPrices() {
        List<Price> prices = priceService.findAll();
        assertEquals(4, prices.size());
    }

    @Test
    public void getPrice_toRequestAtTenAndDayIsFourteen() {
        LocalDateTime expectedStartDate = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
        LocalDateTime expectedEndDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        Optional<Price> price = priceService.getPrice(productId, brandId, applicationDate);

        assertTrue(price.isPresent());
        assertEquals(1L, price.get().getId());
        assertEquals(brandId, price.get().getBrand().getId());
        assertEquals(productId, price.get().getProduct().getId());
        assertEquals(expectedStartDate, price.get().getStartDate());
        assertEquals(expectedEndDate, price.get().getEndDate());
        assertEquals(0, price.get().getPriority());
        assertEquals(35.5, price.get().getPrice());
        assertEquals("EUR", price.get().getCurrency().getIsoCode());
    }

    @Test
    public void getPrice_toRequestAtSixteenAndDayIsFourteen() {
        LocalDateTime expectedStartDate = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
        LocalDateTime expectedEndDate = LocalDateTime.of(2020, 6, 14, 18, 30, 0);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        Optional<Price> price = priceService.getPrice(productId, brandId, applicationDate);

        assertTrue(price.isPresent());
        assertEquals(2L, price.get().getId());
        assertEquals(brandId, price.get().getBrand().getId());
        assertEquals(productId, price.get().getProduct().getId());
        assertEquals(expectedStartDate, price.get().getStartDate());
        assertEquals(expectedEndDate, price.get().getEndDate());
        assertEquals(1, price.get().getPriority());
        assertEquals(25.45, price.get().getPrice());
        assertEquals("EUR", price.get().getCurrency().getIsoCode());
    }

    @Test
    public void getPrice_toRequestAtTwentyOneAndDayIsFourteen() {
        LocalDateTime expectedStartDate = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
        LocalDateTime expectedEndDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0, 0);

        Optional<Price> price = priceService.getPrice(productId, brandId, applicationDate);

        assertTrue(price.isPresent());
        assertEquals(1L, price.get().getId());
        assertEquals(brandId, price.get().getBrand().getId());
        assertEquals(productId, price.get().getProduct().getId());
        assertEquals(expectedStartDate, price.get().getStartDate());
        assertEquals(expectedEndDate, price.get().getEndDate());
        assertEquals(0, price.get().getPriority());
        assertEquals(35.5, price.get().getPrice());
        assertEquals("EUR", price.get().getCurrency().getIsoCode());
    }

    @Test
    public void getPrice_toRequestAtTenAndDayIsFifteen() {
        LocalDateTime expectedStartDate = LocalDateTime.of(2020, 6, 15, 0, 0, 0);
        LocalDateTime expectedEndDate = LocalDateTime.of(2020, 6, 15, 11, 0, 0);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0, 0);

        Optional<Price> price = priceService.getPrice(productId, brandId, applicationDate);

        assertTrue(price.isPresent());
        assertEquals(3L, price.get().getId());
        assertEquals(brandId, price.get().getBrand().getId());
        assertEquals(productId, price.get().getProduct().getId());
        assertEquals(expectedStartDate, price.get().getStartDate());
        assertEquals(expectedEndDate, price.get().getEndDate());
        assertEquals(1, price.get().getPriority());
        assertEquals(30.5, price.get().getPrice());
        assertEquals("EUR", price.get().getCurrency().getIsoCode());
    }

    @Test
    public void getPrice_toRequestAtTwentyOneAndDayIsSixteen() {
        LocalDateTime expectedStartDate = LocalDateTime.of(2020, 6, 15, 16, 0, 0);
        LocalDateTime expectedEndDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0, 0);

        Optional<Price> price = priceService.getPrice(productId, brandId, applicationDate);

        assertTrue(price.isPresent());
        assertEquals(4L, price.get().getId());
        assertEquals(brandId, price.get().getBrand().getId());
        assertEquals(productId, price.get().getProduct().getId());
        assertEquals(expectedStartDate, price.get().getStartDate());
        assertEquals(expectedEndDate, price.get().getEndDate());
        assertEquals(1, price.get().getPriority());
        assertEquals(38.95, price.get().getPrice());
        assertEquals("EUR", price.get().getCurrency().getIsoCode());
    }
}
