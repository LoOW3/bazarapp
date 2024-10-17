package com.loow3.bazarapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String brand;
    private double cost;
    private double qtyLeft;

    // Default constructor
    public Product() {
    }

    // Parameterized constructor
    public Product(String name, double cost, String brand, double qtyLeft) {
        this.name = name;
        this.cost = cost;
        this.brand = brand;
        this.qtyLeft = qtyLeft;
    }

    public Product(Long id, String name, double cost, String brand, double qtyLeft) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.brand = brand;
        this.qtyLeft = qtyLeft;
    }
}
