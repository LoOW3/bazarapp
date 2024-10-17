package com.loow3.bazarapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDate saleDate;
    private double total;
    @OneToMany
    private List<Product> listProducts;
    @OneToOne
    @JoinColumn(name = "clientID", referencedColumnName = "id")
    private Client client;

    public Sale(List<Product> listProducts, Client client, double total, LocalDate saleDate) {
        this.listProducts = listProducts;
        this.client = client;
        this.total = total;
        this.saleDate = saleDate;
    }

    public Sale() {
    }

    public Sale(Long id, LocalDate saleDate, double total, Client client, List<Product> listProducts) {
        this.id = id;
        this.saleDate = saleDate;
        this.total = total;
        this.client = client;
        this.listProducts = listProducts;
    }
}
