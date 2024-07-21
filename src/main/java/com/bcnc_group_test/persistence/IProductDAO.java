package com.bcnc_group_test.persistence;

import com.bcnc_group_test.entities.Product;

import java.util.List;
import java.util.Optional;

public interface IProductDAO {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    void save(Product product);

    void deleteById(Long id);
}
