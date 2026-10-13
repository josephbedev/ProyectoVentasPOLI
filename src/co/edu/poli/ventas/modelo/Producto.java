package co.edu.poli.ventas.modelo;

/**
 * Representa un producto disponible para la venta dentro del sistema.
 * <p>
 * Un producto es un objeto de datos inmutable que agrupa el identificador,
 * el nombre y el precio por unidad, tal como se describe en el formato del
 * archivo de información de productos:
 * {@code IDProducto;NombreProducto;PrecioPorUnidadProducto}.
 * </p>
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class Producto {

    /** Identificador unico del producto. */
    private final int id;

    /** Nombre comercial del producto. */
    private final String nombre;

    /** Precio de venta de una unidad del producto. */
    private final double precioPorUnidad;

    /**
     * Crea un nuevo producto con los datos indicados.
     *
     * @param id              identificador unico del producto
     * @param nombre          nombre comercial del producto
     * @param precioPorUnidad precio de venta de una unidad del producto
     */
    public Producto(int id, String nombre, double precioPorUnidad) {
        this.id = id;
        this.nombre = nombre;
        this.precioPorUnidad = precioPorUnidad;
    }

    /**
     * Obtiene el identificador del producto.
     *
     * @return el identificador unico del producto
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return el nombre comercial del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el precio por unidad del producto.
     *
     * @return el precio de venta de una unidad
     */
    public double getPrecioPorUnidad() {
        return precioPorUnidad;
    }

    /**
     * Construye la representacion en linea plana del producto, lista para
     * escribirse en el archivo de productos, con el formato
     * {@code IDProducto;NombreProducto;PrecioPorUnidadProducto}.
     *
     * @return la linea de texto plano correspondiente a este producto
     */
    public String toLineaArchivo() {
        return id + ";" + nombre + ";" + precioPorUnidad;
    }
}
