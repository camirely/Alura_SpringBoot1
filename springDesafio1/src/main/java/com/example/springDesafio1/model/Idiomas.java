package com.example.springDesafio1.model;

public enum Idiomas {
    INGLES("en", "Inglés"),
    ESPAÑOL("es", "Español"),
    FRANCES("fr", "Francés"),
    PORTUGUES("pt", "Portugués");

    private String codigoOmdb;
    private String nombreEspanol;

    Idiomas(String codigoOmdb, String nombreEspanol) {
        this.codigoOmdb = codigoOmdb;
        this.nombreEspanol = nombreEspanol;
    }

    public String getCodigoOmdb() {
        return codigoOmdb;
    }

    public static Idiomas fromCodigo(String codigo) {
        for (Idiomas idioma : Idiomas.values()) {
            if (idioma.codigoOmdb.equalsIgnoreCase(codigo)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Código de idioma no válido: " + codigo);
    }


}







