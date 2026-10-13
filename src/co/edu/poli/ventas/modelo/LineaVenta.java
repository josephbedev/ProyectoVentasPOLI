package co.edu.poli.ventas.modelo;

/**
 * Representa una linea de un archivo de ventas de un vendedor, con el
 * formato {@code IDProducto;CantidadProductoVendido;}.
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class LineaVenta {

    /** Identificador del producto vendido. */
    private final int idProducto;

    /** Cantidad de unidades vendidas del producto. */
    private final int cantidad;

    /**
     * Crea una nueva linea de venta.
     *
     * @param idProducto identificador del producto vendido
     * @param cantidad   cantidad de unidades vendidas
     */
    public LineaVenta(int idProducto, int cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el identificador del producto vendido.
     *
     * @return el identificador del producto
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Obtiene la cantidad de unidades vendidas.
     *
     * @return la cantidad vendida
     */
    public int getCantidad() {
        return cantidad;
    }
}
