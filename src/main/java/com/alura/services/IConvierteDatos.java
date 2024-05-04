package com.alura.services;

public interface IConvierteDatos {
    // methods
    // Tipos de datos genéricos <T> T
    <T> T obtenerDatos(String json, Class<T> clase);
}
