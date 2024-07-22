package com.bcnc_group_test.controllers;

import com.bcnc_group_test.controllers.dto.ProductDTO;
import com.bcnc_group_test.entities.Product;
import com.bcnc_group_test.services.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @Test
    public void findAll_WithResultsAreValid() throws Exception {
        List<Product> products = List.of(
            new Product(1L, "Product 1"),
            new Product(2L, "Product 2")
        );
        when(productService.findAll()).thenReturn(products);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
            .accept(MediaType.APPLICATION_JSON));

        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Product 1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("Product 2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2));
    }

    @Test
    public void findAll_WithoutResults() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
            .accept(MediaType.APPLICATION_JSON));

        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));
    }

    @Test
    public void findById_ExistingProduct() throws Exception {
        Product product = new Product(1L, "Test Product");
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/{id}", 1).accept(MediaType.APPLICATION_JSON)
        );

        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Test Product"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1));
    }

    @Test
    public void findById_NonExistingProduct() throws Exception {
        Long productId = 999L;
        Product product = new Product(1L, "Test Product");
        when(productService.findById(1L)).thenReturn(Optional.of(product));

        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/{id}", productId)
                .accept(MediaType.APPLICATION_JSON)
        );
        result
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product " + productId + " not found"));
    }

    @Test
    void save_createProduct_WhenProductNameIsValid() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Jeans");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(productDTO));
        ResultActions result = mockMvc.perform(request);

        result
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product created successfully"));
    }

    @Test
    void save_ShouldReturnBadRequest_WhenProductNameIsInvalid() throws Exception {
        ProductDTO productDTO = new ProductDTO("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(productDTO));
        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product name is required"));
    }

    @Test
    public void delete_ExistingProduct() throws Exception {
        Long productId = 1L;
        when(productService.findById(productId)).thenReturn(Optional.of(new Product(productId, "Test Product")));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", productId));

        result
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    public void delete_NonExistingProductReturnsNotFound() throws Exception {
        Long productId = 999L;
        when(productService.findById(productId)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", productId));

        result
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product " + productId + " not found"));
    }

    @Test
    public void delete_ProductWithInvalidIdReturnsError() throws Exception {
        Long productId = 0L;
        when(productService.findById(productId)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", productId));

        result
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid id " + productId));
    }
}
