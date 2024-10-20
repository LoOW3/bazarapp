package com.loow3.bazarapp.controller;

import com.loow3.bazarapp.model.Sale;
import com.loow3.bazarapp.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
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
    @GetMapping("/ventas/productos/{codigo_venta}")
    public ResponseEntity<Object> getSaleProducts(@PathVariable Long codigo_venta){
        return sS.getSaleProducts(codigo_venta);
    }
    @GetMapping("/ventas/date")
    public ResponseEntity<Object> getTotalAmountsSale(@RequestParam("fecha_venta") String fecha_venta) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(fecha_venta);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use 'yyyy-MM-dd'.");
        }

        return sS.getTotalAmountsSale(localDate);
    }

    @GetMapping("/ventas/mayor_venta")
    public ResponseEntity<Object> getMayorSale(){
        return sS.getMayorSale();
    }
    @DeleteMapping("/ventas/eliminar/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable Long id){
        sS.deleteSale(id);
        return ResponseEntity.ok("successfully deleted");
    }

    @PatchMapping("/ventas/editar/{id}")
    public ResponseEntity<Object> editSale(@PathVariable Long id,
                                             @RequestBody Map<String, Object> fields){
        return sS.updateSale(id, fields);
    }
}
