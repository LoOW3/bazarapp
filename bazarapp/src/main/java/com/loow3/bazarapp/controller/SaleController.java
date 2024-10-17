package com.loow3.bazarapp.controller;

import com.loow3.bazarapp.model.Sale;
import com.loow3.bazarapp.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SaleController {
    @Autowired
    ISaleService sS;

    @PostMapping("/ventas/crear")
    public ResponseEntity<Object> createSale(@RequestBody Sale sale){
        return sS.createSale(sale);
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<Sale>> listSales(){
        return ResponseEntity.ok(sS.listSales());
    }

    @GetMapping("/ventas/{id}")
    public ResponseEntity<Optional<Sale>> getSale(@PathVariable Long id){
        return ResponseEntity.ok(sS.getSale(id));
    }

    @DeleteMapping("/ventas/eliminar/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable Long id){
        sS.deleteSale(id);
        return ResponseEntity.ok("successfully deleted");
    }

    @PutMapping("/ventas/editar/{id}")
    public ResponseEntity<Object> editSale(@PathVariable Long id,
                                             @RequestBody Sale sale){
        return sS.updateSale(id, sale);
    }
}
