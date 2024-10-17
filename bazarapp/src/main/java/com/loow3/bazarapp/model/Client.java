package com.loow3.bazarapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String lastName;
    private String dni;

    public Client() {
    }

    public Client(String name, String lastName, String dni) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
    }

    public Client(Long id, String name, String lastName, String dni) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
    }
}
