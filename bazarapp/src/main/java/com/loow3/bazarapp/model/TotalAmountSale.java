package com.loow3.bazarapp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter @Setter
public class TotalAmountSale {
    private Double totalAmountSale;
    private LocalDate date;
    private int totalSales;

    public TotalAmountSale() {
        this.totalAmountSale = 0.0;  // Initialize to zero
        this.totalSales = 0;
    }

    public TotalAmountSale(LocalDate date, int totalSales) {
        this.totalAmountSale = 0.0;
        this.date = date;
        this.totalSales = totalSales;
    }
}
