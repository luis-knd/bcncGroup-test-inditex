package com.bcnc_group_test.persistence;

import com.bcnc_group_test.entities.Brand;

import java.util.List;
import java.util.Optional;

public interface IBrandDAO {

    List<Brand> findAll();

    Optional<Brand> findById(Long id);

    void save(Brand brand);

    void deleteById(Long id);
}
