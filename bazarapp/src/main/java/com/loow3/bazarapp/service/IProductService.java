package com.loow3.bazarapp.service;

import com.loow3.bazarapp.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    public Optional<Product> getProduct(Long id);
    public List<Product> listProducts();
    public void createProduct(Product product);
    public void deleteProduct(Long id);
    public ResponseEntity<Object> updateProduct(Long id, Product product);
}
