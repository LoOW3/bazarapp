package com.loow3.bazarapp.service;

import com.loow3.bazarapp.dto.SaleDTO;
import com.loow3.bazarapp.model.TotalAmountSale;
import com.loow3.bazarapp.model.Product;
import com.loow3.bazarapp.model.Sale;
import com.loow3.bazarapp.repository.IProductRepository;
import com.loow3.bazarapp.repository.ISaleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
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

    @Override
    public ResponseEntity<Object> getSaleProducts(Long codigoVenta) {
        HashMap<String, Object> datos = new HashMap<String, Object>();
        Optional<Sale> sale = sR.findById(codigoVenta);

        if(sale.isEmpty()){
            datos.put("error", "Sale with ID " + codigoVenta + " doesn't exist");
            return new ResponseEntity<>(datos, HttpStatus.NOT_FOUND);
        }
            datos.put("status", "success");
            datos.put("sale", sale);

        return new ResponseEntity<>(datos, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<Object> getTotalAmountsSale(LocalDate fecha_venta) {
        HashMap<String, Object> datos = new HashMap<String, Object>();
        TotalAmountSale totAmountS = new TotalAmountSale();
        List<Sale> itermidiateSaleList = new ArrayList<Sale>();
        List<Sale> listSales = sR.findAll();

        for(Sale sale : listSales){
            if(sale.getSaleDate().equals(fecha_venta)){
                itermidiateSaleList.add(sale);
            }
        }

        for(Sale sale : itermidiateSaleList){
            totAmountS.setTotalAmountSale(totAmountS.getTotalAmountSale() + sale.getTotal());
            totAmountS.setTotalSales(totAmountS.getTotalSales() + 1);
        }

        datos.put("Total amount", totAmountS.getTotalAmountSale());
        datos.put("Total sales", totAmountS.getTotalSales());
        return new ResponseEntity<>(
                datos,
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Object> getMayorSale() {
        HashMap<String, Object> datos = new HashMap<String, Object>();
        List<Sale> sales = sR.findAll();

        SaleDTO mayorSale = new SaleDTO();

        Sale maxSale = new Sale();

        for(Sale sale : sales){
            if(sale.getTotal() > maxSale.getTotal()){
                maxSale = sale;
            }

        }
        if(maxSale.getId() != -1){
            int totProducts = 0;
            for(Product product: maxSale.getListProducts()){
                totProducts =+ 1;
            }
            mayorSale.setSaleID(maxSale.getId());
            mayorSale.setTotal(maxSale.getTotal());
            mayorSale.setClientName(maxSale.getClient().getName());
            mayorSale.setClientLastName(maxSale.getClient().getLastName());
            mayorSale.setTotalProducts(totProducts);
        }

        datos.put("status", "success");
        datos.put("max_sale", mayorSale);
        return new ResponseEntity<>(
                datos,
                HttpStatus.FOUND
                );
    }



}
