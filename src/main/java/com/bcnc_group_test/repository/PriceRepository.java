package com.bcnc_group_test.repository;

import com.bcnc_group_test.entities.Price;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PriceRepository extends CrudRepository<Price, Long> {
    @Query(value = "SELECT price.* "+
        "FROM price price " +
        "WHERE price.product_id = :productId " +
        "AND price.brand_id = :brandId " +
        "AND price.start_date <= :applicationDate " +
        "AND price.end_date >= :applicationDate " +
        "ORDER BY price.priority desc LIMIT 1",
        nativeQuery = true)
    Optional<Price> getPrice(Long productId, Long brandId, LocalDateTime applicationDate);
}
