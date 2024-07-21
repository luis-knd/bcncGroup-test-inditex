package com.bcnc_group_test.persistence.impl;

import com.bcnc_group_test.entities.Price;
import com.bcnc_group_test.persistence.IPriceDAO;
import com.bcnc_group_test.repository.PriceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PriceDAOImpl implements IPriceDAO {

    private final PriceRepository priceRepository;

    public PriceDAOImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public List<Price> findAll() {
        return (List<Price>) priceRepository.findAll();
    }

    @Override
    public Optional<Price> findById(Long id) {
        return priceRepository.findById(id);
    }

    @Override
    public void save(Price price) {
        priceRepository.save(price);
    }

    @Override
    public void deleteById(Long id) {
        priceRepository.deleteById(id);
    }

    @Override
    public Optional<Price> getPrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        return priceRepository.getPrice(productId, brandId, applicationDate);
    }
}
