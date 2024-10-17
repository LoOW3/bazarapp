package com.loow3.bazarapp.service;

import com.loow3.bazarapp.model.Product;
import com.loow3.bazarapp.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Service
public class ProductService implements IProductService{
    @Autowired
    IProductRepository pR;

    @Override
    public Optional<Product> getProduct(Long id) {
        return pR.findById(id);
    }

    @Override
    public List<Product> listProducts() {
        return pR.findAll();
    }

    @Override
    public void createProduct(Product product) {
        HashMap<String, Object> datos = new HashMap<String,Object>();
        pR.save(product);
        datos.put("status", "ok");
        datos.put("product", product);
        new ResponseEntity<>(datos, HttpStatus.CREATED);
    }

    @Override
    public void deleteProduct(Long id) {
        pR.deleteById(id);
    }

    @Override
    public ResponseEntity<Object> updateProduct(Long id, Product product) {
        HashMap<String, Object> datos = new HashMap<String, Object>();

        boolean productExist = pR.existsById(id);
        if(!productExist){
            datos.put("update", "fail");
            datos.put("cause", "product doesn't exits");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }else{
            datos.put("update", "success");
            datos.put("product", product);
            this.createProduct(product);
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.OK
            );
        }
    }
}
