package com.loow3.bazarapp.service;

import com.loow3.bazarapp.model.Product;
import com.loow3.bazarapp.model.Sale;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ISaleService {
    public Optional<Sale> getSale(Long id);
    public List<Sale> listSales();
    public ResponseEntity<Object> createSale(Sale sale);
    public void deleteSale(Long id);
    public ResponseEntity<Object> updateSale(Long id, Map<String, Object> fields);
}
