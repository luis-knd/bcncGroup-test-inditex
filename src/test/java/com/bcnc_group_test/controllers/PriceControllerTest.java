package com.bcnc_group_test.controllers;

import com.bcnc_group_test.entities.*;
import com.bcnc_group_test.services.impl.PriceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceServiceImpl priceService;

    private long productId;
    private long brandId;
    private LocalDateTime applicationDate;

    @BeforeEach
    public void setUp() {
        productId = 35455L;
        brandId = 1L;
    }

    private ResultActions performGetPriceRequest(long productId, long brandId, LocalDateTime applicationDate) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/prices/get-price")
                .param("productId", String.valueOf(productId))
                .param("brandId", String.valueOf(brandId))
                .param("applicationDate", applicationDate.toString())
                .accept(MediaType.APPLICATION_JSON)
        );
    }

    private Price createPrice(long id, Brand brand, LocalDateTime startDate, LocalDateTime endDate, long priceList, Product product, int priority, double price, CurrencyCode currency) {
        return new Price(id, brand, startDate, endDate, priceList, product, priority, price, currency);
    }

    @Test
    public void getPrice_withCorrectDateAndNonExistentProductShouldBeObtainAnEmptyPrice() throws Exception {
        applicationDate = LocalDateTime.of(2024, 7, 21, 13, 0, 0);
        when(priceService.getPrice(productId, brandId, applicationDate)).thenReturn(Optional.empty());

        ResultActions result = performGetPriceRequest(productId, brandId, applicationDate);
        result
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Price not found"));
    }

    @Test
    public void getPrice_withOnlySendYearShouldBeObtainAnError() throws Exception {
        String applicationDate = "2020";

        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/prices/get-price")
                .param("productId", String.valueOf(productId))
                .param("brandId", String.valueOf(brandId))
                .param("applicationDate", applicationDate)
                .accept(MediaType.APPLICATION_JSON)
        );
        result
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value("The value " + applicationDate + " in the field applicationDate is invalid. Expected format: yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void getPrice_withTwoEqualsRegistersShouldBeReturnTheRegisterWithGreaterPriority() throws Exception {
        List<Price> priceList = List.of(
            createPrice(
                1L,
                new Brand(1L, "Zara"),
                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                0L,
                new Product(35455L, "Shirt"),
                1,
                85.5,
                CurrencyCode.USD
            ),
            createPrice(
                2L,
                new Brand(1L, "Zara"),
                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                1L,
                new Product(35455L, "Shirt"),
                0,
                33.95,
                CurrencyCode.USD
            )
        );
        Price priceExpected = priceList.get(0);
        applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        when(priceService.getPrice(productId, brandId, applicationDate)).thenReturn(Optional.of(priceExpected));

        ResultActions result = performGetPriceRequest(productId, brandId, applicationDate);
        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value(productId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.brand.id").value(brandId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.price").value(85.5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.currency").value("USD"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value("2020-06-14T00:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.endDate").value("2020-12-31T23:59:59"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceList").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void getPrice_firstCaseRequestedInTheTest() throws Exception {
        Price priceExpected = createPrice(
            1L,
            new Brand(1L, "Zara"),
            LocalDateTime.of(2020, 6, 14, 0, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            1L,
            new Product(35455L, "Shirt"),
            0,
            35.5,
            CurrencyCode.EUR
        );
        applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        when(priceService.getPrice(productId, brandId, applicationDate)).thenReturn(Optional.of(priceExpected));

        ResultActions result = performGetPriceRequest(productId, brandId, applicationDate);
        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value(productId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.brand.id").value(brandId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.price").value(35.5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.currency").value("EUR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value("2020-06-14T00:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.endDate").value("2020-12-31T23:59:59"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceList").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void getPrice_secondCaseRequestedInTheTest() throws Exception {
        Price priceExpected = createPrice(
            1L,
            new Brand(1L, "Zara"),
            LocalDateTime.of(2020, 6, 14, 15, 0, 0),
            LocalDateTime.of(2020, 6, 14, 18, 30, 0),
            2L,
            new Product(35455L, "Shirt"),
            1,
            25.45,
            CurrencyCode.EUR
        );
        applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
        when(priceService.getPrice(productId, brandId, applicationDate)).thenReturn(Optional.of(priceExpected));

        ResultActions result = performGetPriceRequest(productId, brandId, applicationDate);
        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value(productId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.brand.id").value(brandId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.price").value(25.45))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.currency").value("EUR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value("2020-06-14T15:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.endDate").value("2020-06-14T18:30:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceList").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void getPrice_thirdCaseRequestedInTheTest() throws Exception {
        Price priceExpected = createPrice(
            1L,
            new Brand(1L, "Zara"),
            LocalDateTime.of(2020, 6, 14, 0, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            1L,
            new Product(35455L, "Shirt"),
            0,
            35.5,
            CurrencyCode.EUR
        );
        applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0, 0);
        when(priceService.getPrice(productId, brandId, applicationDate)).thenReturn(Optional.of(priceExpected));

        ResultActions result = performGetPriceRequest(productId, brandId, applicationDate);
        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value(productId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.brand.id").value(brandId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.price").value(35.5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.currency").value("EUR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value("2020-06-14T00:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.endDate").value("2020-12-31T23:59:59"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceList").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void getPrice_fourthCaseRequestedInTheTest() throws Exception {
        Price priceExpected = createPrice(
            3L,
            new Brand(1L, "Zara"),
            LocalDateTime.of(2020, 6, 15, 0, 0, 0),
            LocalDateTime.of(2020, 6, 15, 11, 0, 0),
            3L,
            new Product(35455L, "Shirt"),
            1,
            30.5,
            CurrencyCode.EUR
        );
        applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0, 0);
        when(priceService.getPrice(productId, brandId, applicationDate)).thenReturn(Optional.of(priceExpected));

        ResultActions result = performGetPriceRequest(productId, brandId, applicationDate);
        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value(productId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.brand.id").value(brandId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.price").value(30.5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.currency").value("EUR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value("2020-06-15T00:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.endDate").value("2020-06-15T11:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceList").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void getPrice_fifthCaseRequestedInTheTest() throws Exception {
        Price priceExpected = createPrice(
            3L,
            new Brand(1L, "Zara"),
            LocalDateTime.of(2020, 6, 15, 16, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            4L,
            new Product(35455L, "Shirt"),
            1,
            38.95,
            CurrencyCode.EUR
        );
        applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0, 0);
        when(priceService.getPrice(productId, brandId, applicationDate)).thenReturn(Optional.of(priceExpected));

        ResultActions result = performGetPriceRequest(productId, brandId, applicationDate);
        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value(productId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.brand.id").value(brandId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.price").value(38.95))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.currency").value("EUR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.startDate").value("2020-06-15T16:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.endDate").value("2020-12-31T23:59:59"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceList").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }
}
