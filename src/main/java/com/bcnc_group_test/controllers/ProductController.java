package com.bcnc_group_test.controllers;

import com.bcnc_group_test.controllers.dto.ProductDTO;
import com.bcnc_group_test.controllers.dto.mapper.ProductMapper;
import com.bcnc_group_test.entities.Product;
import com.bcnc_group_test.handler.ResponseHandler;
import com.bcnc_group_test.services.IProductService;
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

@Tag(name = "Product", description = "Product management API")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all products", tags = {"getAll"})
    @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
    )
    @GetMapping("")
    public ResponseEntity<?> findAll() {
        List<ProductDTO> productDTOList = productService.findAll()
            .stream()
            .map(ProductMapper::toProductDTO)
            .toList();
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, productDTOList, productDTOList.size());
    }

    @Operation(summary = "Get a product by ID", description = "Retrieve a product by its ID", tags = {"get"})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductDTO productDTO = ProductMapper.toProductDTO(product);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, productDTO, 1);
        }
        return ResponseHandler.generateResponse("Product " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new product", description = "Create a new product with the provided data", tags = {"save"})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Product created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product data to be created",
        required = true, content = @Content(schema = @Schema(implementation = ProductDTO.class)))
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ProductDTO productDTO) {
        if (productDTO.getName() == null || productDTO.getName().isBlank()) {
            return ResponseHandler.generateResponse("Product name is required", HttpStatus.BAD_REQUEST);
        }
        productService.save(ProductMapper.toProductEntity(productDTO));
        return ResponseHandler.generateResponse("Product created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Update a product by ID", description = "Update an existing product by its ID", tags = {"update"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product data to be updated",
        required = true, content = @Content(schema = @Schema(implementation = ProductDTO.class)))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent() && !productDTO.getName().isBlank()) {
            Product product = productOptional.get();
            product.setName(productDTO.getName());
            productService.save(product);
            return ResponseHandler.generateResponse("Product updated successfully", HttpStatus.OK);
        }
        return productDTO.getName().isBlank() ?
            ResponseHandler.generateResponse("Product name is required", HttpStatus.BAD_REQUEST) :
            ResponseHandler.generateResponse("Product " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete a product by ID", description = "Delete a product by its ID", tags = {"delete"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
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

        if (productService.findById(id).isEmpty()) {
            return ResponseHandler.generateResponse("Product " + id + " not found", HttpStatus.NOT_FOUND);
        }

        productService.deleteById(id);
        return ResponseHandler.generateResponse("Product deleted successfully", HttpStatus.OK);
    }
}
