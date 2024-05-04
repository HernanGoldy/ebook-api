package com.alura.principal;

import com.alura.models.DatosGenerales;
import com.alura.models.DatosLibro;
import com.alura.services.ConsumoAPI;
import com.alura.services.ConvierteDatosImp;

import java.util.Comparator;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatosImp conversor = new ConvierteDatosImp();
    private static final String URL_BASE = "https://gutendex.com/books/";

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
    }
}
