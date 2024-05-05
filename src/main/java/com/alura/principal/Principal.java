package com.alura.principal;

import com.alura.models.DatosGenerales;
import com.alura.models.DatosLibro;
import com.alura.services.ConsumoAPI;
import com.alura.services.ConvierteDatosImp;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatosImp conversor = new ConvierteDatosImp();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String BUSQUEDA = "?search=";

    // methods
    public void mostrarMenu() {
        String json = consumoApi.obtenerDatos(URL_BASE);
        System.out.println(json);
        DatosGenerales datos = conversor.obtenerDatos(json, DatosGenerales.class);
        System.out.println(datos);

        // Top 10 libros más descargados
        System.out.println("Los 10 libros más descargados son: ");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo())
                .forEach(System.out::println);

        // Búsqueda de libro por nombre
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        String tituloLibro = teclado.nextLine().replace(" ", "+");
        json = consumoApi.obtenerDatos(URL_BASE + BUSQUEDA + tituloLibro);
        DatosGenerales datosBusqueda = conversor.obtenerDatos(json, DatosGenerales.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("El libro " + libroBuscado.get().titulo() + " fue encontrado.");
        } else {
            System.out.println("El libro " + tituloLibro + " no fue encontrado.");
        }

        // Trabajando con estadísticas
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d -> d.numeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibro::numeroDeDescargas));
        System.out.println("El promedio de descargas es: " + est.getAverage());
        System.out.println("La cantidad máxima de descargas es: " + est.getMax());
        System.out.println("La cantidad mínima de descargas es: " + est.getMin());
        System.out.println("Cantidad de registros evaluados: " + est.getCount());
    }
}
