package com.bcnc_group_test.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "price")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Brand is required")
    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @NotBlank(message = "start date is required")
    @DateTimeFormat(pattern = "YYYY-MM-dd-hh.mm.ss")
    private LocalDateTime startDate;

    @NotBlank(message = "end date is required")
    @DateTimeFormat(pattern = "YYYY-MM-dd-hh.mm.ss")
    private LocalDateTime endDate;

    @NotBlank(message = "price list is required")
    private Long priceList;

    @NotBlank(message = "product id is required")
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @NotBlank(message = "priority is required")
    @Column(columnDefinition = "int default 0")
    private int priority;

    @NotBlank(message = "price is required")
    private double price;

    @NotBlank(message = "currency is required")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;
}
