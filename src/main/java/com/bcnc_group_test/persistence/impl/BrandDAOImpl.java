package com.bcnc_group_test.persistence.impl;

import com.bcnc_group_test.entities.Brand;
import com.bcnc_group_test.persistence.IBrandDAO;
import com.bcnc_group_test.repository.BrandRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BrandDAOImpl implements IBrandDAO {

    private final BrandRepository brandRepository;

    public BrandDAOImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> findAll() {
        return (List<Brand>) brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}
