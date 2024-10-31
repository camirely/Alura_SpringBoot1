package com.example.springDesafio1.principal;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.example.springDesafio1.Reposity.LibrosRepository;
import com.example.springDesafio1.model.*;
import com.example.springDesafio1.service.ConsumoAPI;
import com.example.springDesafio1.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private final String API_KEY = "TU-APIKEY-OMDB";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    private LibrosRepository repositorio;

    private List<Libro> libros;

    public Principal(LibrosRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;

        try {
            while (opcion != 0) {
                var menu = """
                        1 - Buscar Liros por titulo
                        2 - Listar libros registrados
                        3 - Listar autores registrados
                        4 - Listar autores vivos en determinado año
                        5 - Listar por idiomas
                                           
                        0 - Salir
                        """;
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibrosPorTitulo();
                        break;
                    case 2:
                        mostrarLirosRegistrados();
                        break;
                    case 3:
                        mostrarAutoresRegistrados();
                        break;
                    case 4:
                        mostrarAutoresVivosEnAno();
                        break;
                    case 5:
                        mostrarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Opción inválida. Por favor, ingrese un número entero.");
        }


    }

    private void buscarLibrosPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();

        // Obtener datos de búsqueda
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        // Buscar el libro en los resultados
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibros datosLibro = libroBuscado.get();
            List<DatosLibros> newLibros = new ArrayList<>();
            newLibros.add(datosLibro);

            // Convertir DatosLibros a Libro y guardar en base de datos
            Libro nuevoLibro = new Libro();
            nuevoLibro.setTitulo(datosLibro.titulo());

            try {
                nuevoLibro.setIdiomas(Idiomas.fromCodigo(datosLibro.idiomas().get(0)));
            } catch (IllegalArgumentException e) {
                System.out.println("Idioma inválido: " + datosLibro.idiomas());
                return; // Manejo de error si el idioma no es válido
            }

            nuevoLibro.setNumeroDeDescargas(datosLibro.numeroDeDescargas());

            // Convertir lista de DatosAutores a lista de Autor
            List<Autor> autores = datosLibro.autor().stream()
                    .map(datosAutor -> {
                        Autor autor = new Autor();
                        autor.setName(datosAutor.name());
                        autor.setFechaNacimiento(datosAutor.fechaNacimiento());
                        autor.setFechaMuerte(datosAutor.fechaMuerte());
                        return autor;
                    })
                    .collect(Collectors.toList());

            nuevoLibro.setAutores(autores);

            // Guardar el libro en la base de datos
            repositorio.save(nuevoLibro);
            System.out.println("Libro guardado exitosamente.");

            // Mostrar los detalles del libro guardado
            System.out.println("Libro nuevo:");
            mostrarDetallesLibro(nuevoLibro);
        } else {
            System.out.println("No se encontró ningún libro con el título: " + tituloLibro);
        }

    }


    private void mostrarLirosRegistrados() {
        List<Libro> libros = repositorio.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Libro libro : libros) {
                mostrarDetallesLibro(libro);
            }
        }


    }

    private void mostrarAutoresRegistrados() {
        List<Libro> libros = repositorio.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            mostrarDetallesAutor(libros);
        }
    }


    private void mostrarAutoresVivosEnAno() {
        System.out.println("Ingrese el año vivo de autor(as) que desea buscar:");
        var ano = teclado.nextInt();

        List<Autor> autores = repositorio.findAutoresVivosDesde(ano);

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            mostrarDetallesAutorVivo(autores);
        }
    }



    private void mostrarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma para buscar el libro");
        String languages = String.join("\n", "-ingles", "-español", "-francés", "-portugués");
        System.out.println(languages);

        String idiomaInput = teclado.nextLine().toUpperCase();
        Idiomas idioma;

        idioma = Idiomas.valueOf(idiomaInput);


        List<Libro> libros = repositorio.findLibrosPorIdioma(idioma);
        System.out.println("Idioma seleccionado: " + idioma.name());

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados con ese idioma.");
        } else {
            for (Libro libro : libros) {
                mostrarDetallesLibro(libro);
            }
        }
    }





    private void mostrarDetallesLibro(Libro libro) {
        System.out.println("******************");
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Idiomas: " + libro.getIdiomas());
        System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());

        String autores = libro.getAutores().stream()
                .map(autor -> reordenarNombre(autor.getName()))
                .collect(Collectors.joining(", "));

        System.out.println("Autor: " + autores);
        System.out.println("******************");
    }

    private void mostrarDetallesAutor(List<Libro> libros) {
        for (Libro libro : libros) {
            if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
                for (Autor autor : libro.getAutores()) {
                    System.out.println("Autor: " + reordenarNombre(autor.getName()));
                    System.out.println("Fecha de Nacimiento: " + (autor.getFechaNacimiento() != null ? autor.getFechaNacimiento() : "Desconocida"));
                    System.out.println("Fecha de Muerte: " + (autor.getFechaMuerte() != null ? autor.getFechaMuerte() : "Desconocida"));
                    System.out.println("Libro: : " + libro.getTitulo());
                    System.out.println("--------------------");
                }
            } else {
                System.out.println("No hay autores disponibles.");
            }
            System.out.println("******************");
        }
    }

    private void mostrarDetallesAutorVivo(List<Autor> autores) {
        for (Autor autor : autores) {
            System.out.println("Autor: " + reordenarNombre(autor.getName()));
            System.out.println("Fecha de Nacimiento: " + (autor.getFechaNacimiento() != null ? autor.getFechaNacimiento() : "Desconocida"));
            System.out.println("Fecha de Muerte: " + (autor.getFechaMuerte() != null ? autor.getFechaMuerte() : "Desconocida"));

            // Mostrar los libros del autor
            if (autor.getLibros() != null && !autor.getLibros().getAutores().isEmpty()) {

                System.out.println("Libro: " + autor.getLibros().getTitulo());

            } else {
                System.out.println("No hay libros disponibles.");
            }
            System.out.println("--------------------");
        }
        System.out.println("******************");
    }

    public static String reordenarNombre(String nombreCompleto) {

        String[] partes = nombreCompleto.split(", ");


        if (partes.length == 2) {
            return partes[1] + " " + partes[0];
        }

        return nombreCompleto;
    }





}