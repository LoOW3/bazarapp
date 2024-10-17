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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

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
    public ResponseEntity<Object> updateSale(Long id, Map<String, Object> fields) {

        HashMap<String, Object> datos = new HashMap<String, Object>();

        Optional<Sale> saleOptional = sR.findById(id);

        if(saleOptional.isEmpty()){
            datos.put("update", "fail");
            datos.put("cause", "sale doesn't exits");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }else{
            Sale saleExists = saleOptional.get();
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Product.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, saleExists, value);
                }
            });
            sR.save(saleExists);
            datos.put("update", "success");
            datos.put("sale updated", saleExists );
            return new ResponseEntity<>(datos, HttpStatus.OK);
        }
    }

}
