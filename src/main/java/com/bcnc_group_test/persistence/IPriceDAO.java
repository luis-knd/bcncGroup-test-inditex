package com.bcnc_group_test.persistence;

import com.bcnc_group_test.entities.Price;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IPriceDAO {

    List<Price> findAll();

    Optional<Price> findById(Long id);

    void save(Price product);

    void deleteById(Long id);

    Optional<Price> getPrice(Long productId, Long brandId, LocalDateTime applicationDate);
}