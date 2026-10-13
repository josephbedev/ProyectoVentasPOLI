package co.edu.poli.ventas.reportes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.edu.poli.ventas.modelo.LineaVenta;
import co.edu.poli.ventas.modelo.Producto;
import co.edu.poli.ventas.modelo.ResultadoProducto;
import co.edu.poli.ventas.modelo.ResultadoVendedor;
import co.edu.poli.ventas.modelo.Vendedor;

/**
 * Calcula los consolidados de ventas y escribe los dos reportes CSV que
 * exige el proyecto:
 * <ol>
 * <li>Reporte de vendedores, ordenado de mayor a menor por el dinero
 * recaudado, con el formato {@code NombresApellidos;TotalRecaudado}.</li>
 * <li>Reporte de productos vendidos, ordenado de mayor a menor por la
 * cantidad vendida, con el formato {@code NombreProducto;PrecioPorUnidad}.</li>
 * </ol>
 * Los campos se separan con punto y coma (;), igual que en los archivos de
 * entrada. Esta clase no debe instanciarse.
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public final class GeneradorReportes {

    /** Nombre del archivo CSV con el reporte de vendedores. */
    public static final String REPORTE_VENDEDORES = "reporte_vendedores.csv";

    /** Nombre del archivo CSV con el reporte de productos. */
    public static final String REPORTE_PRODUCTOS = "reporte_productos.csv";

    private GeneradorReportes() {
    }

    /**
     * Calcula el dinero recaudado por cada vendedor (precio por unidad por
     * cantidad, sumado sobre todas sus ventas) y los ordena de mayor a menor.
     * Los vendedores sin ventas aparecen con total cero.
     *
     * @param vendedores vendedores validos
     * @param productos  productos validos
     * @param ventas     lineas de venta agrupadas por documento de vendedor
     * @return los resultados ordenados por total recaudado descendente
     */
    public static List<ResultadoVendedor> calcularVentasPorVendedor(Map<Long, Vendedor> vendedores,
            Map<Integer, Producto> productos, Map<Long, List<LineaVenta>> ventas) {
        List<ResultadoVendedor> resultados = new ArrayList<>();
        for (Vendedor vendedor : vendedores.values()) {
            double total = 0;
            List<LineaVenta> lineas = ventas.get(vendedor.getNumeroDocumento());
            if (lineas != null) {
                for (LineaVenta linea : lineas) {
                    total += productos.get(linea.getIdProducto()).getPrecioPorUnidad() * linea.getCantidad();
                }
            }
            resultados.add(new ResultadoVendedor(vendedor, redondear(total)));
        }
        Collections.sort(resultados, new Comparator<ResultadoVendedor>() {
            @Override
            public int compare(ResultadoVendedor a, ResultadoVendedor b) {
                return Double.compare(b.getTotalRecaudado(), a.getTotalRecaudado());
            }
        });
        return resultados;
    }

    /**
     * Calcula la cantidad total vendida de cada producto, sumando las ventas
     * de todos los vendedores, y los ordena de mayor a menor. Solo se incluyen
     * los productos que tuvieron al menos una venta.
     *
     * @param productos productos validos
     * @param ventas    lineas de venta agrupadas por documento de vendedor
     * @return los resultados ordenados por cantidad vendida descendente
     */
    public static List<ResultadoProducto> calcularCantidadesPorProducto(Map<Integer, Producto> productos,
            Map<Long, List<LineaVenta>> ventas) {
        Map<Integer, Integer> cantidades = new LinkedHashMap<>();
        for (List<LineaVenta> lineas : ventas.values()) {
            for (LineaVenta linea : lineas) {
                Integer actual = cantidades.get(linea.getIdProducto());
                cantidades.put(linea.getIdProducto(), (actual == null ? 0 : actual) + linea.getCantidad());
            }
        }

        List<ResultadoProducto> resultados = new ArrayList<>();
        for (Producto producto : productos.values()) {
            Integer cantidad = cantidades.get(producto.getId());
            if (cantidad != null && cantidad > 0) {
                resultados.add(new ResultadoProducto(producto, cantidad));
            }
        }
        Collections.sort(resultados, new Comparator<ResultadoProducto>() {
            @Override
            public int compare(ResultadoProducto a, ResultadoProducto b) {
                return Integer.compare(b.getCantidadVendida(), a.getCantidadVendida());
            }
        });
        return resultados;
    }

    /**
     * Escribe el reporte CSV de vendedores con el formato
     * {@code NombresApellidos;TotalRecaudado}.
     *
     * @param resultados resultados ya ordenados
     * @param archivo    ruta del archivo a escribir
     * @throws IOException si no se puede escribir el archivo
     */
    public static void escribirReporteVendedores(List<ResultadoVendedor> resultados, Path archivo)
            throws IOException {
        crearCarpetaPadre(archivo);
        try (BufferedWriter escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            for (ResultadoVendedor r : resultados) {
                escritor.write(r.getVendedor().getNombreCompleto() + ";" + formatear(r.getTotalRecaudado()));
                escritor.newLine();
            }
        }
    }

    /**
     * Escribe el reporte CSV de productos con el formato
     * {@code NombreProducto;PrecioPorUnidad}.
     *
     * @param resultados resultados ya ordenados
     * @param archivo    ruta del archivo a escribir
     * @throws IOException si no se puede escribir el archivo
     */
    public static void escribirReporteProductos(List<ResultadoProducto> resultados, Path archivo)
            throws IOException {
        crearCarpetaPadre(archivo);
        try (BufferedWriter escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            for (ResultadoProducto r : resultados) {
                escritor.write(r.getProducto().getNombre() + ";" + formatear(r.getProducto().getPrecioPorUnidad()));
                escritor.newLine();
            }
        }
    }

    /**
     * Da formato a un valor monetario con dos decimales y punto decimal,
     * independiente de la configuracion regional del equipo.
     *
     * @param valor valor a formatear
     * @return el valor con dos decimales, por ejemplo {@code 15230.50}
     */
    public static String formatear(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private static void crearCarpetaPadre(Path archivo) throws IOException {
        Path padre = archivo.toAbsolutePath().getParent();
        if (padre != null) {
            Files.createDirectories(padre);
        }
    }
}
