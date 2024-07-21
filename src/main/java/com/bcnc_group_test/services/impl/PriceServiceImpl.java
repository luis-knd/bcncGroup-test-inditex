package com.bcnc_group_test.services.impl;

import com.bcnc_group_test.entities.Price;
import com.bcnc_group_test.persistence.IPriceDAO;
import com.bcnc_group_test.services.IPriceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PriceServiceImpl implements IPriceService {

    private final IPriceDAO priceDAO;

    public PriceServiceImpl(IPriceDAO priceDAO) {
        this.priceDAO = priceDAO;
    }

    @Override
    public List<Price> findAll() {
        return priceDAO.findAll();
    }

    @Override
    public Optional<Price> findById(Long id) {
        return priceDAO.findById(id);
    }

    @Override
    public void save(Price price) {
        priceDAO.save(price);
    }

    @Override
    public void deleteById(Long id) {
        priceDAO.deleteById(id);
    }

    @Override
    public Optional<Price> getPrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        return priceDAO.getPrice(productId, brandId, applicationDate);
    }
}
