package com.bcnc_group_test.controllers;

import com.bcnc_group_test.controllers.dto.BrandDTO;
import com.bcnc_group_test.controllers.dto.mapper.BrandMapper;
import com.bcnc_group_test.entities.Brand;
import com.bcnc_group_test.handler.ResponseHandler;
import com.bcnc_group_test.services.IBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Brand", description = "Brand management API")
@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final IBrandService brandService;

    public BrandController(IBrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "Get all brands", description = "Retrieve a list of all brands", tags = {"getAll"})
    @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandDTO.class))
    )
    @GetMapping("")
    public ResponseEntity<?> findAll() {
        List<BrandDTO> brandDTOList = brandService.findAll()
            .stream()
            .map(BrandMapper::toBrandDTO)
            .toList();
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, brandDTOList, brandDTOList.size());
    }

    @Operation(summary = "Get a brand by ID", description = "Retrieve a brand by its ID", tags = {"get"})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Brand found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Brand not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Brand> brandOptional = brandService.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            BrandDTO brandDTO = BrandMapper.toBrandDTO(brand);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, brandDTO, 1);
        }
        return ResponseHandler.generateResponse("Brand " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new brand", description = "Create a new brand with the provided data", tags = {"save"})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Brand created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BrandDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Brand data to be created",
        required = true, content = @Content(schema = @Schema(implementation = BrandDTO.class)))
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody BrandDTO brandDTO) {
        if (brandDTO.getName() == null || brandDTO.getName().isBlank()) {
            return ResponseHandler.generateResponse("Brand name is required", HttpStatus.BAD_REQUEST);
        }

        brandService.save(BrandMapper.toBrandEntity(brandDTO));
        return ResponseHandler.generateResponse("Brand created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Update a brand by ID", description = "Update an existing brand by its ID", tags = {"update"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Brand not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Brand data to be updated",
        required = true, content = @Content(schema = @Schema(implementation = BrandDTO.class)))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrandById(@PathVariable Long id, @RequestBody BrandDTO brandDTO) {
        Optional<Brand> brandOptional = brandService.findById(id);
        if (brandOptional.isPresent() && !brandDTO.getName().isBlank()) {
            Brand brand = brandOptional.get();
            brand.setName(brandDTO.getName());
            brandService.save(brand);
            return ResponseHandler.generateResponse("Brand updated successfully", HttpStatus.OK);
        }
        return brandDTO.getName().isBlank() ?
            ResponseHandler.generateResponse("Brand name is required", HttpStatus.BAD_REQUEST) :
            ResponseHandler.generateResponse("Brand " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete a brand by ID", description = "Delete a brand by its ID", tags = {"delete"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseHandler.generateResponse("Invalid id " + id, HttpStatus.BAD_REQUEST);
        }

        if (brandService.findById(id).isEmpty()) {
            return ResponseHandler.generateResponse("Brand " + id + " not found", HttpStatus.NOT_FOUND);
        }

        brandService.deleteById(id);
        return ResponseHandler.generateResponse("Brand deleted successfully", HttpStatus.OK);
    }
}
