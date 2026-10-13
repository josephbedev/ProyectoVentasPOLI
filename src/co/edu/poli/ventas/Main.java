package co.edu.poli.ventas;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * Clase principal del proyecto (clase {@code main} del enunciado).
 * <p>
 * Lee los archivos planos generados por {@link GenerateInfoFiles} y produce
 * los reportes finales en la carpeta {@code data/reportes}:
 * </p>
 * <ul>
 * <li>{@code reporte_vendedores.csv}: vendedores ordenados de mayor a menor
 * por dinero recaudado ({@code NombresApellidos;TotalRecaudado}).</li>
 * <li>{@code reporte_productos.csv}: productos vendidos ordenados de mayor a
 * menor por cantidad vendida ({@code NombreProducto;PrecioPorUnidad}).</li>
 * </ul>
 * <p>
 * El programa no solicita informacion al usuario. Al terminar informa por
 * consola si la ejecucion fue exitosa, las advertencias de datos invalidos
 * que encontro y, en caso de fallo, el motivo del error.
 * </p>
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class Main {

    /** Carpeta donde se encuentran los archivos de entrada. */
    private static final Path CARPETA_DATOS = Paths.get("data");

    /** Carpeta donde se escriben los reportes. */
    private static final Path CARPETA_REPORTES = CARPETA_DATOS.resolve("reportes");

    /**
     * Ejecuta el procesamiento completo: lectura, validacion, calculo y
     * escritura de los reportes.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        try {
            LectorArchivos lector = new LectorArchivos(CARPETA_DATOS);
            Map<Integer, Producto> productos = lector.leerProductos();
            Map<Long, Vendedor> vendedores = lector.leerVendedores();
            Map<Long, List<LineaVenta>> ventas = lector.leerVentas(productos, vendedores);

            if (productos.isEmpty() || vendedores.isEmpty()) {
                throw new IllegalStateException("No hay productos o vendedores validos para procesar.");
            }

            List<ResultadoVendedor> reporteVendedores =
                    GeneradorReportes.calcularVentasPorVendedor(vendedores, productos, ventas);
            List<ResultadoProducto> reporteProductos =
                    GeneradorReportes.calcularCantidadesPorProducto(productos, ventas);

            Path archivoVendedores = CARPETA_REPORTES.resolve(GeneradorReportes.REPORTE_VENDEDORES);
            Path archivoProductos = CARPETA_REPORTES.resolve(GeneradorReportes.REPORTE_PRODUCTOS);
            GeneradorReportes.escribirReporteVendedores(reporteVendedores, archivoVendedores);
            GeneradorReportes.escribirReporteProductos(reporteProductos, archivoProductos);

            imprimirResumen(reporteVendedores, reporteProductos, lector.getAdvertencias());
            System.out.println();
            System.out.println("Reportes generados con exito:");
            System.out.println("  " + archivoVendedores);
            System.out.println("  " + archivoProductos);
        } catch (IOException | RuntimeException excepcion) {
            System.err.println("No fue posible generar los reportes: " + excepcion.getMessage());
        }
    }

    /**
     * Muestra por consola el ranking de vendedores, el de productos y las
     * advertencias encontradas en los datos.
     */
    private static void imprimirResumen(List<ResultadoVendedor> vendedores, List<ResultadoProducto> productos,
            List<String> advertencias) {
        System.out.println("=== Vendedores por dinero recaudado ===");
        int posicion = 1;
        for (ResultadoVendedor r : vendedores) {
            System.out.println(posicion++ + ". " + r.getVendedor().getNombreCompleto() + " - $"
                    + GeneradorReportes.formatear(r.getTotalRecaudado()));
        }

        System.out.println();
        System.out.println("=== Productos por cantidad vendida ===");
        posicion = 1;
        for (ResultadoProducto r : productos) {
            System.out.println(posicion++ + ". " + r.getProducto().getNombre() + " - "
                    + r.getCantidadVendida() + " unidades");
        }

        if (!advertencias.isEmpty()) {
            System.out.println();
            System.out.println("=== Advertencias (" + advertencias.size() + " datos omitidos) ===");
            for (String advertencia : advertencias) {
                System.out.println("- " + advertencia);
            }
        }
    }
}
