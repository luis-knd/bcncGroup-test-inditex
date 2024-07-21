package com.bcnc_group_test.services.impl;

import com.bcnc_group_test.entities.Brand;
import com.bcnc_group_test.persistence.IBrandDAO;
import com.bcnc_group_test.services.IBrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements IBrandService {

    private final IBrandDAO brandDAO;

    public BrandServiceImpl(IBrandDAO brandDAO) {
        this.brandDAO = brandDAO;
    }

    @Override
    public List<Brand> findAll() {
        return brandDAO.findAll();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandDAO.findById(id);
    }

    @Override
    public void save(Brand brand) {
        brandDAO.save(brand);
    }

    @Override
    public void deleteById(Long id) {
        brandDAO.deleteById(id);
    }
}
