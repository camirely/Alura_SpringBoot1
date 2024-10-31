package com.example.springDesafio1.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idiomas idiomas;
    private Double numeroDeDescargas;

    @OneToMany(mappedBy = "libros", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;


    public Libro(){}

    public Libro(String titulo, Idiomas idiomas, Double numeroDeDescargas, List<Autor> autores) {
        this.titulo = titulo;
        this.idiomas = idiomas;
        this.numeroDeDescargas = numeroDeDescargas;
        this.autores = autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idiomas getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Idiomas idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        autores.forEach(e -> e.setLibros(this));
        this.autores = autores;
    }
}
