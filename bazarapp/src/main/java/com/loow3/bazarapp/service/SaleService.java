package com.loow3.bazarapp.service;

import com.loow3.bazarapp.model.Product;
import com.loow3.bazarapp.model.Sale;
import com.loow3.bazarapp.repository.IProductRepository;
import com.loow3.bazarapp.repository.ISaleRepository;
import com.loow3.bazarapp.repository.ISaleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Service
public class SaleService implements ISaleService{
    @Autowired
    ISaleRepository sR;
    @Autowired
    IProductRepository pR;

    @Override
    public Optional<Sale> getSale(Long id) {
        return sR.findById(id);
    }

    @Override
    public List<Sale> listSales() {
        return sR.findAll();
    }

    @Override
    public ResponseEntity<Object> createSale(@NotNull Sale sale) {
        HashMap<String, Object> datos = new HashMap<>();

        List<Product> listProducts = sale.getListProducts();
        List<String> outOfStockProducts = new ArrayList<>();

        // Verificación de stock
        for (Product product : listProducts) {
            Long id = product.getId();
            Product productFullData = pR.findById(id).orElse(null);
            if (productFullData == null) {
                outOfStockProducts.add("Producto no encontrado con ID: " + id);
                continue;
            }
            if (productFullData.getQtyLeft() < 1) {
                outOfStockProducts.add("No hay stock de: " + productFullData.getName());
            }
        }

        if (!outOfStockProducts.isEmpty()) {
            datos.put("error", true);
            datos.put("causes", outOfStockProducts);
            return new ResponseEntity<>(datos, HttpStatus.CONFLICT);
        }

        // Actualización de stock
        for (Product product : listProducts) {
            Long id = product.getId();
            Product productFullData = pR.findById(id).orElseThrow(); // Ahora es seguro usarlo
            productFullData.setQtyLeft(productFullData.getQtyLeft() - 1);
            pR.save(productFullData);
        }

        // Guardar la venta
        sR.save(sale);
        datos.put("status", "ok");
        datos.put("sale", sale);
        return new ResponseEntity<>(datos, HttpStatus.CREATED);
    }


    @Override
    public void deleteSale(Long id) {
        sR.deleteById(id);
    }

    @Override
    public ResponseEntity<Object> updateSale(Long id, Sale sale) {
        HashMap<String, Object> datos = new HashMap<String, Object>();

        boolean saleExists = sR.existsById(id);
        if(!saleExists){
            datos.put("update", "fail");
            datos.put("cause", "sale doesn't exits");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }else{
            datos.put("update", "success");
            datos.put("sale", sale);
            this.createSale(sale);
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.OK
            );
        }
    }

}
