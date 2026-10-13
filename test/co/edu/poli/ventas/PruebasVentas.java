package co.edu.poli.ventas;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import co.edu.poli.ventas.lector.LectorArchivos;
import co.edu.poli.ventas.modelo.LineaVenta;
import co.edu.poli.ventas.modelo.Producto;
import co.edu.poli.ventas.modelo.ResultadoProducto;
import co.edu.poli.ventas.modelo.ResultadoVendedor;
import co.edu.poli.ventas.modelo.Vendedor;
import co.edu.poli.ventas.reportes.GeneradorReportes;

/**
 * Pruebas del procesamiento de ventas. No usan librerias externas: basta con
 * ejecutar esta clase ({@code Run As > Java Application}) y revisar la
 * consola. Cada prueba crea sus propios archivos en una carpeta temporal,
 * con datos conocidos, para comparar el resultado con el valor esperado.
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class PruebasVentas {

    private static int aprobadas = 0;
    private static int fallidas = 0;

    /**
     * Ejecuta todas las pruebas e imprime el resultado.
     *
     * @param args no se utilizan
     * @throws IOException si no se pueden crear los archivos temporales
     */
    public static void main(String[] args) throws IOException {
        pruebaCalculoYOrdenVendedores();
        pruebaCalculoYOrdenProductos();
        pruebaDatosInvalidosSeOmiten();
        pruebaVariosArchivosMismoVendedor();
        pruebaVendedorSinVentas();
        pruebaFormatoReportesCsv();
        pruebaArchivoProductosInexistente();

        System.out.println();
        System.out.println("Pruebas aprobadas: " + aprobadas + " | fallidas: " + fallidas);
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    /** Datos base: 3 productos, 2 vendedores y un archivo de ventas cada uno. */
    private static Path crearDatosBase() throws IOException {
        Path carpeta = Files.createTempDirectory("ventas_prueba");
        escribir(carpeta.resolve("productos.txt"), "1;Arroz;1000", "2;Cafe;5000", "3;Leche;2500");
        escribir(carpeta.resolve("vendedores.txt"), "CC;111;Ana;Gomez Diaz", "CE;222;Luis;Perez Rojas");
        escribir(carpeta.resolve("ventas/ventas_Ana_111.txt"), "CC;111", "1;10;", "2;1;");
        escribir(carpeta.resolve("ventas/ventas_Luis_222.txt"), "CE;222", "2;4;", "3;2;");
        return carpeta;
    }

    private static void pruebaCalculoYOrdenVendedores() throws IOException {
        Datos d = cargar(crearDatosBase());
        List<ResultadoVendedor> r = GeneradorReportes.calcularVentasPorVendedor(d.vendedores, d.productos, d.ventas);
        // Luis: 4*5000 + 2*2500 = 25000 ; Ana: 10*1000 + 1*5000 = 15000
        verificar("Vendedor con mas ventas va primero", "Luis Perez Rojas", r.get(0).getVendedor().getNombreCompleto());
        verificar("Total de Luis", 25000.0, r.get(0).getTotalRecaudado());
        verificar("Total de Ana", 15000.0, r.get(1).getTotalRecaudado());
    }

    private static void pruebaCalculoYOrdenProductos() throws IOException {
        Datos d = cargar(crearDatosBase());
        List<ResultadoProducto> r = GeneradorReportes.calcularCantidadesPorProducto(d.productos, d.ventas);
        // Arroz 10, Cafe 5, Leche 2
        verificar("Producto mas vendido", "Arroz", r.get(0).getProducto().getNombre());
        verificar("Cantidad de Cafe (suma de dos vendedores)", 5, r.get(1).getCantidadVendida());
        verificar("Producto menos vendido", "Leche", r.get(2).getProducto().getNombre());
    }

    private static void pruebaDatosInvalidosSeOmiten() throws IOException {
        Path carpeta = crearDatosBase();
        escribir(carpeta.resolve("productos.txt"), "1;Arroz;1000", "2;Cafe;-5", "x;Malo;10", "1;Repetido;20");
        escribir(carpeta.resolve("ventas/ventas_Ana_111.txt"), "CC;111", "1;3;", "9;2;", "1;-4;", "1;abc;");
        escribir(carpeta.resolve("ventas/ventas_Fantasma_999.txt"), "CC;999", "1;5;");
        LectorArchivos lector = new LectorArchivos(carpeta);
        Datos d = cargar(lector);
        verificar("Solo queda el producto valido", 1, d.productos.size());
        verificar("Solo queda la venta valida de Ana", 1, d.ventas.get(111L).size());
        verificar("Vendedor inexistente no se procesa", false, d.ventas.containsKey(999L));
        verificar("Se registran advertencias", true, lector.getAdvertencias().size() >= 7);
    }

    private static void pruebaVariosArchivosMismoVendedor() throws IOException {
        Path carpeta = crearDatosBase();
        escribir(carpeta.resolve("ventas/ventas_Ana_111_parte2.txt"), "CC;111", "1;5;");
        Datos d = cargar(carpeta);
        List<ResultadoVendedor> r = GeneradorReportes.calcularVentasPorVendedor(d.vendedores, d.productos, d.ventas);
        // Ana: 15000 + 5*1000 = 20000
        verificar("Ventas de dos archivos se acumulan", 20000.0, r.get(1).getTotalRecaudado());
    }

    private static void pruebaVendedorSinVentas() throws IOException {
        Path carpeta = crearDatosBase();
        Files.delete(carpeta.resolve("ventas/ventas_Luis_222.txt"));
        Datos d = cargar(carpeta);
        List<ResultadoVendedor> r = GeneradorReportes.calcularVentasPorVendedor(d.vendedores, d.productos, d.ventas);
        verificar("Vendedor sin ventas aparece con 0", 0.0, r.get(1).getTotalRecaudado());
    }

    private static void pruebaFormatoReportesCsv() throws IOException {
        Path carpeta = crearDatosBase();
        Datos d = cargar(carpeta);
        Path vend = carpeta.resolve("reportes/reporte_vendedores.csv");
        Path prod = carpeta.resolve("reportes/reporte_productos.csv");
        GeneradorReportes.escribirReporteVendedores(
                GeneradorReportes.calcularVentasPorVendedor(d.vendedores, d.productos, d.ventas), vend);
        GeneradorReportes.escribirReporteProductos(
                GeneradorReportes.calcularCantidadesPorProducto(d.productos, d.ventas), prod);
        verificar("Formato reporte vendedores",
                Arrays.asList("Luis Perez Rojas;25000.00", "Ana Gomez Diaz;15000.00"),
                Files.readAllLines(vend, StandardCharsets.UTF_8));
        verificar("Formato reporte productos",
                Arrays.asList("Arroz;1000.00", "Cafe;5000.00", "Leche;2500.00"),
                Files.readAllLines(prod, StandardCharsets.UTF_8));
    }

    private static void pruebaArchivoProductosInexistente() throws IOException {
        Path carpeta = Files.createTempDirectory("ventas_vacia");
        boolean lanzoError = false;
        try {
            new LectorArchivos(carpeta).leerProductos();
        } catch (IOException e) {
            lanzoError = true;
        }
        verificar("Sin productos.txt se informa el error", true, lanzoError);
    }

    // ----- utilidades -----

    private static class Datos {
        Map<Integer, Producto> productos;
        Map<Long, Vendedor> vendedores;
        Map<Long, List<LineaVenta>> ventas;
    }

    private static Datos cargar(Path carpeta) throws IOException {
        return cargar(new LectorArchivos(carpeta));
    }

    private static Datos cargar(LectorArchivos lector) throws IOException {
        Datos d = new Datos();
        d.productos = lector.leerProductos();
        d.vendedores = lector.leerVendedores();
        d.ventas = lector.leerVentas(d.productos, d.vendedores);
        return d;
    }

    private static void escribir(Path archivo, String... lineas) throws IOException {
        Files.createDirectories(archivo.getParent());
        Files.write(archivo, Arrays.asList(lineas), StandardCharsets.UTF_8);
    }

    private static void verificar(String nombre, Object esperado, Object obtenido) {
        if (esperado.equals(obtenido)) {
            aprobadas++;
            System.out.println("[OK]    " + nombre);
        } else {
            fallidas++;
            System.out.println("[FALLA] " + nombre + " -> esperado: " + esperado + ", obtenido: " + obtenido);
        }
    }
}
