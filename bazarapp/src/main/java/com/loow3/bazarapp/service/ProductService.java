package com.loow3.bazarapp.service;

import com.loow3.bazarapp.model.Product;
import com.loow3.bazarapp.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

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
    public ResponseEntity<Object> getLowStock() {
        HashMap<String, Object> datos = new HashMap<String, Object>();
        List<Product> products = this.listProducts();
        List<Product> lowStockProducts = new ArrayList<Product>();

        for(Product product : products){
            if(product.getQtyLeft() < 5){
                lowStockProducts.add(product);
            }
        }
        datos.put("status", "ok");
        datos.put("low_stock", lowStockProducts);
        return new ResponseEntity<>(
                datos,
                HttpStatus.FOUND
        );
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
    public ResponseEntity<Object> updateProduct(Long id, Map<String, Object> fields) {

        HashMap<String, Object> datos = new HashMap<String, Object>();

        Optional<Product> productOptional = pR.findById(id);

        if(productOptional.isEmpty()){
            datos.put("update", "fail");
            datos.put("cause", "product doesn't exits");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }else{
            Product productExists = productOptional.get();
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Product.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, productExists, value);
                }
            });
            pR.save(productExists);
            datos.put("update", "success");
            datos.put("product updated", productExists );
            return new ResponseEntity<>(datos, HttpStatus.OK);
        }
    }
}
