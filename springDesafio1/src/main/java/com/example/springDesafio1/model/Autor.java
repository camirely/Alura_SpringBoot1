package com.example.springDesafio1.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String fechaNacimiento;
    private String fechaMuerte;

    @ManyToOne()
    private Libro libros;

    public Autor(){}

    public Autor(String name, String fechaNacimiento, String fechaMuerte, Libro libros) {
        this.name = name;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaMuerte = fechaMuerte;
        this.libros = libros;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(String fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public Libro getLibros() {
        return libros;
    }

    public void setLibros(Libro libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "name='" + name + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", fechaMuerte='" + fechaMuerte + '\'' +
                ", libros=" + libros +
                '}';
    }
}
