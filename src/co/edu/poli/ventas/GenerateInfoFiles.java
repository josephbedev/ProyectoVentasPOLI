package co.edu.poli.ventas;

import java.io.IOException;
import java.util.List;

import co.edu.poli.ventas.generador.GeneradorArchivos;
import co.edu.poli.ventas.modelo.Vendedor;

/**
 * Punto de entrada de la Entrega 1 del proyecto. Al ejecutarse, esta clase
 * genera todos los archivos planos pseudoaleatorios que servira como entrada
 * para la clase {@code main} (correspondiente a las siguientes entregas):
 * <ol>
 * <li>El archivo de productos disponibles ({@code data/productos.txt}).</li>
 * <li>El archivo de informacion de vendedores ({@code data/vendedores.txt}).</li>
 * <li>Un archivo de ventas por cada vendedor generado, ubicado en la carpeta
 * {@code data/ventas}.</li>
 * </ol>
 * <p>
 * Esta clase no solicita ningun dato al usuario: todas las cantidades a
 * generar estan definidas como constantes dentro de la misma clase, para
 * facilitar su ajuste si se requiere una masa de datos de prueba distinta.
 * </p>
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class GenerateInfoFiles {

    /** Cantidad de productos que se generaran para las pruebas. */
    private static final int CANTIDAD_PRODUCTOS = 15;

    /** Cantidad de vendedores que se generaran para las pruebas. */
    private static final int CANTIDAD_VENDEDORES = 8;

    /** Cantidad minima de ventas pseudoaleatorias por vendedor. */
    private static final int MINIMO_VENTAS_POR_VENDEDOR = 5;

    /** Cantidad maxima de ventas pseudoaleatorias por vendedor. */
    private static final int MAXIMO_VENTAS_POR_VENDEDOR = 15;

    /**
     * Metodo principal de la Entrega 1: genera el catalogo de productos, la
     * informacion de los vendedores y un archivo de ventas pseudoaleatorio
     * por cada vendedor generado. Al finalizar, informa por consola si el
     * proceso fue exitoso o si ocurrio algun error.
     *
     * @param args argumentos de linea de comandos; no se utilizan, pues el
     *             programa no solicita informacion al usuario
     */
    public static void main(String[] args) {
        try {
            GeneradorArchivos.createProductsFile(CANTIDAD_PRODUCTOS);

            List<Vendedor> vendedoresGenerados = GeneradorArchivos.createSalesManInfoFile(CANTIDAD_VENDEDORES);

            for (Vendedor vendedor : vendedoresGenerados) {
                int cantidadVentas = generarCantidadVentasEnRango();
                GeneradorArchivos.createSalesMenFile(
                        cantidadVentas,
                        vendedor.getNombreCompleto(),
                        vendedor.getNumeroDocumento());
            }

            System.out.println("Generacion de archivos finalizada con exito.");
            System.out.println("Productos generados: " + CANTIDAD_PRODUCTOS);
            System.out.println("Vendedores generados: " + CANTIDAD_VENDEDORES);
            System.out.println("Los archivos quedaron disponibles en la carpeta 'data'.");
        } catch (IOException | IllegalArgumentException excepcion) {
            System.err.println("Ocurrio un error generando los archivos de prueba: " + excepcion.getMessage());
        }
    }

    /**
     * Genera una cantidad de ventas pseudoaleatoria dentro del rango definido
     * por {@link #MINIMO_VENTAS_POR_VENDEDOR} y {@link #MAXIMO_VENTAS_POR_VENDEDOR}.
     *
     * @return la cantidad de ventas generada para un vendedor
     */
    private static int generarCantidadVentasEnRango() {
        int rango = MAXIMO_VENTAS_POR_VENDEDOR - MINIMO_VENTAS_POR_VENDEDOR + 1;
        return MINIMO_VENTAS_POR_VENDEDOR + (int) (Math.random() * rango);
    }
}
