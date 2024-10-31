package com.example.springDesafio1.Reposity;

import com.example.springDesafio1.model.Autor;
import com.example.springDesafio1.model.Idiomas;
import com.example.springDesafio1.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibrosRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT a FROM Autor a JOIN FETCH a.libros WHERE a.fechaNacimiento <= :anio AND (a.fechaMuerte IS NULL OR a.fechaMuerte > :anio)")
    List<Autor> findAutoresVivosDesde(int anio);

    @Query("SELECT l FROM Libro l JOIN l.autores a WHERE l.idiomas = :idiomas")
    List<Libro> findLibrosPorIdioma(Idiomas idiomas);




}
