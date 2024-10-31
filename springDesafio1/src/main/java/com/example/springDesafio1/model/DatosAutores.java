package com.example.springDesafio1.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DatosAutores(
        @JsonAlias("name") String name,
        @JsonAlias("birth_year") String fechaNacimiento,
        @JsonAlias("death_year") String fechaMuerte
) {


}
