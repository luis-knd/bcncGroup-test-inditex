package com.bcnc_group_test.controllers;

import com.bcnc_group_test.controllers.dto.PriceDTO;
import com.bcnc_group_test.controllers.dto.PriceToApplyDTO;
import com.bcnc_group_test.controllers.dto.mapper.PriceMapper;
import com.bcnc_group_test.entities.CurrencyCode;
import com.bcnc_group_test.entities.Price;
import com.bcnc_group_test.handler.ResponseHandler;
import com.bcnc_group_test.services.IPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "Price", description = "Price management API")
@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

    private final IPriceService priceService;

    public PriceController(IPriceService priceService) {
        this.priceService = priceService;
    }

    @Operation(summary = "Get all prices", description = "Retrieve a list of all prices", tags = {"getAll"})
    @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDTO.class))
    )
    @GetMapping("")
    public ResponseEntity<?> findAll() {
        List<PriceDTO> priceDTOList = priceService.findAll()
            .stream()
            .map(PriceMapper::toPriceDTO)
            .toList();
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, priceDTOList, priceDTOList.size());
    }

    @Operation(summary = "Get a price by ID", description = "Retrieve a price by its ID", tags = {"get"})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Price found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Price not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Price> priceOptional = priceService.findById(id);
        if (priceOptional.isPresent()) {
            Price price = priceOptional.get();
            PriceDTO priceDTO = PriceMapper.toPriceDTO(price);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, priceDTO, 1);
        }
        return ResponseHandler.generateResponse("Price " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new price", description = "Create a new price with the provided data", tags = {"save"})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Price created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Price data to be created",
        required = true, content = @Content(schema = @Schema(implementation = PriceDTO.class)))
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody PriceDTO priceDTO) {
        if (priceDTO.getPrice() <= 0) {
            return ResponseHandler.generateResponse("Price value is required", HttpStatus.BAD_REQUEST);
        }

        priceService.save(PriceMapper.toPriceEntity(priceDTO));
        return ResponseHandler.generateResponse("Price created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Update a price by ID", description = "Update an existing price by its ID", tags = {"update"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Price updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Price not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Price data to be updated",
        required = true, content = @Content(schema = @Schema(implementation = PriceDTO.class)))
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePriceById(@PathVariable Long id, @RequestBody PriceDTO priceDTO) {
        Optional<Price> priceOptional = priceService.findById(id);
        if (priceOptional.isPresent() && priceDTO.getPrice() > 0) {
            Price price = priceOptional.get();
            price.setStartDate(priceDTO.getStartDate());
            price.setEndDate(priceDTO.getEndDate());
            price.setPriceList(priceDTO.getPriceList());
            price.setPriority(priceDTO.getPriority());
            price.setPrice(priceDTO.getPrice());
            price.setCurrency(CurrencyCode.valueOf(String.valueOf(priceDTO.getCurrency())));
            priceService.save(price);
            return ResponseHandler.generateResponse("Price updated successfully", HttpStatus.OK);
        }
        return priceDTO.getPrice() <= 0 ?
            ResponseHandler.generateResponse("Price value is required", HttpStatus.BAD_REQUEST) :
            ResponseHandler.generateResponse("Price " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete a price by ID", description = "Delete a price by its ID", tags = {"delete"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Price deleted successfully"),
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

        if (priceService.findById(id).isEmpty()) {
            return ResponseHandler.generateResponse("Price " + id + " not found", HttpStatus.NOT_FOUND);
        }

        priceService.deleteById(id);
        return ResponseHandler.generateResponse("Price deleted successfully", HttpStatus.OK);
    }


    /**
     * Retrieve the price to be applied for a specific product and brand at a given application date.
     *
     * @param productId the ID of the product for which the price is being requested
     * @param brandId the ID of the brand for which the price is being requested
     * @param applicationDate the date and time for which the price is being requested, in the format "yyyy-MM-dd HH:mm:ss"
     * @return a {@link ResponseEntity} containing a {@link PriceToApplyDTO} with the price information if found,
     *         or a response indicating that the price was not found
     *
     * @apiNote This method is documented for OpenAPI.
     */
    @Operation(summary = "Get applicable price", description = "Retrieve the price to be applied for a specific product and brand at a given application date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceToApplyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Price not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)))
    })
    @GetMapping("/get-price")
    public ResponseEntity<?> getPrice(
        @RequestParam(defaultValue = "35455", name = "productId") Long productId,
        @RequestParam(defaultValue = "1", name = "brandId")  Long brandId,
        @RequestParam(defaultValue = "2020-06-14 00:00:00", name = "applicationDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime applicationDate
    ) {
        Optional<Price> optionalPrice = priceService.getPrice(productId, brandId, applicationDate);
        if (optionalPrice.isPresent()) {
            PriceToApplyDTO priceToApplyDTO = PriceMapper.toPriceToApplyDTO(optionalPrice.get());
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, priceToApplyDTO, 1);
        }
        return ResponseHandler.generateResponse("Price not found", HttpStatus.NOT_FOUND);
    }
}
