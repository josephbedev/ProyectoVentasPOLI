package co.edu.poli.ventas.modelo;

/**
 * Resultado consolidado de un producto para el reporte de productos: el
 * producto y la cantidad total de unidades vendidas entre todos los
 * vendedores.
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class ResultadoProducto {

    /** Producto al que corresponde el resultado. */
    private final Producto producto;

    /** Cantidad total de unidades vendidas del producto. */
    private final int cantidadVendida;

    /**
     * Crea un nuevo resultado de producto.
     *
     * @param producto        producto al que corresponde el resultado
     * @param cantidadVendida cantidad total de unidades vendidas
     */
    public ResultadoProducto(Producto producto, int cantidadVendida) {
        this.producto = producto;
        this.cantidadVendida = cantidadVendida;
    }

    /**
     * Obtiene el producto.
     *
     * @return el producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Obtiene la cantidad total vendida.
     *
     * @return la cantidad de unidades vendidas
     */
    public int getCantidadVendida() {
        return cantidadVendida;
    }
}
