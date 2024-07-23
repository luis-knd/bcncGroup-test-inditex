package com.bcnc_group_test.controllers;

import com.bcnc_group_test.controllers.dto.BrandDTO;
import com.bcnc_group_test.entities.Brand;
import com.bcnc_group_test.services.impl.BrandServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandServiceImpl brandService;

    @Test
    public void findAll_WithResultsAreValid() throws Exception {
        List<Brand> brands = List.of(
            new Brand(1L, "Brand 1"),
            new Brand(2L, "Brand 2")
        );
        when(brandService.findAll()).thenReturn(brands);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands")
            .accept(MediaType.APPLICATION_JSON));

        result
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Brand 1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("Brand 2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2));
    }

    @Test
    public void findAll_WithoutResults() throws Exception {
        when(brandService.findAll()).thenReturn(List.of());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/brands")
            .accept(MediaType.APPLICATION_JSON));

        result
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));
    }

    @Test
    public void findById_ExistingBrand() throws Exception {
        Brand brand = new Brand(1L, "Test Brand");
        when(brandService.findById(1L)).thenReturn(Optional.of(brand));

        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/brands/{id}", 1).accept(MediaType.APPLICATION_JSON)
        );

        result
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Test Brand"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1));
    }

    @Test
    public void findById_NonExistingBrand() throws Exception {
        Long brandId = 999L;
        Brand brand = new Brand(1L, "Test Brand");
        when(brandService.findById(1L)).thenReturn(Optional.of(brand));

        ResultActions result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/brands/{id}", brandId)
                .accept(MediaType.APPLICATION_JSON)
        );
        result
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand " + brandId + " not found"));
    }

    @Test
    public void save_createBrand_WhenBrandNameIsValid() throws Exception {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Pull&Bear");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(brandDTO));
        ResultActions result = mockMvc.perform(request);

        result
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand created successfully"));
    }

    @Test
    public void save_ShouldReturnBadRequest_WhenBrandNameIsInvalid() throws Exception {
        BrandDTO brandDTO = new BrandDTO("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(brandDTO));
        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand name is required"));
    }

    @Test
    public void updateById_ExistingBrandReturnsOk() throws Exception {
        Long id = 1L;
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("New Brand Name");
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(brandDTO.getName());
        when(brandService.findById(id)).thenReturn(Optional.of(brand));
        doAnswer(invocation -> {
            System.out.println("Brand updated successfully");
            return null;
        }).when(brandService).save(brand);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/v1/brands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(brandDTO));
        ResultActions result = mockMvc.perform(request);

        result
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand updated successfully"));
        Assertions.assertEquals(brand.getName(), brandDTO.getName());
    }

    @Test
    public void updateById_withoutExistingBrandReturnsNotFound() throws Exception {
        Long id = 1L;
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("New Brand Name");
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(brandDTO.getName());
        when(brandService.findById(id)).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            System.out.println("Brand " + id + " not found");
            return null;
        }).when(brandService).save(brand);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/v1/brands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(brandDTO));
        ResultActions result = mockMvc.perform(request);

        result
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand " + id + " not found"));
    }

    @Test
    public void updateById_withBrandWithoutNameReturnsValidationError() throws Exception {
        Long id = 1L;
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("");
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(brandDTO.getName());
        when(brandService.findById(id)).thenReturn(Optional.of(brand));
        doAnswer(invocation -> {
            System.out.println("Brand name is required");
            return null;
        }).when(brandService).save(brand);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/v1/brands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(brandDTO));
        ResultActions result = mockMvc.perform(request);

        result
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand name is required"));
    }

    @Test
    public void delete_ExistingBrand() throws Exception {
        Long brandId = 1L;
        when(brandService.findById(brandId)).thenReturn(Optional.of(new Brand(brandId, "Test Brand")));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/brands/{id}", brandId));

        result
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand deleted successfully"));
    }

    @Test
    public void delete_NonExistingBrandReturnsNotFound() throws Exception {
        Long brandId = 999L;
        when(brandService.findById(brandId)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/brands/{id}", brandId));

        result
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Brand " + brandId + " not found"));
    }

    @Test
    public void delete_BrandWithInvalidIdReturnsError() throws Exception {
        Long brandId = -10L;
        when(brandService.findById(brandId)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/brands/{id}", brandId));

        result
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid id " + brandId));
    }

    @Test
    public void delete_BrandWithNullIdReturnsError() throws Exception {
        Long brandId = null;
        when(brandService.findById(brandId)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/brands/{id}", brandId));

        result
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The sent value is invalid"));
    }
}
