package com.loow3.bazarapp.controller;

import com.loow3.bazarapp.model.Product;
import com.loow3.bazarapp.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductController {
    @Autowired
    IProductService pS;

    @PostMapping("/productos/crear")
    public ResponseEntity<String> createProduct(@RequestBody Product product){
        pS.createProduct(product);
        return ResponseEntity.ok("successfully created");
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Product>> listProducts(){
        return ResponseEntity.ok(pS.listProducts());
    }
    
    @GetMapping("/productos/{id}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(pS.getProduct(id));
    }

    @GetMapping("/productos/falta_stock")
    public ResponseEntity<Object> getLowStock(){

        return pS.getLowStock();
    }

    @DeleteMapping("/productos/eliminar/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        pS.deleteProduct(id);
        return ResponseEntity.ok("successfully deleted");
    }

    @PatchMapping("/productos/editar/{id}")
    public ResponseEntity<Object> editProduct(@PathVariable Long id,
                                               @RequestBody Map<String, Object> fields){
        return pS.updateProduct(id, fields);
    }
}
