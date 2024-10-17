package com.loow3.bazarapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleDTO {
    //codigo_venta, el total, la cantidad de productos, el nombre del cliente y el
    //apellido del cliente de la venta con el monto más alto de todas.

    private Long saleID;
    private double total;
    private int totalProducts;
    private String clientName;
    private String clientLastName;

    public SaleDTO(Long saleID, double total, int totalProducts, String clientLastName, String clientName) {
        this.saleID = saleID;
        this.total = total;
        this.totalProducts = totalProducts;
        this.clientLastName = clientLastName;
        this.clientName = clientName;
    }
}
