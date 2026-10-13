package co.edu.poli.ventas.modelo;

/**
 * Resultado consolidado de un vendedor para el reporte de vendedores: el
 * vendedor y el total de dinero que recaudo con todas sus ventas.
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class ResultadoVendedor {

    /** Vendedor al que corresponde el resultado. */
    private final Vendedor vendedor;

    /** Total de dinero recaudado por el vendedor. */
    private final double totalRecaudado;

    /**
     * Crea un nuevo resultado de vendedor.
     *
     * @param vendedor       vendedor al que corresponde el resultado
     * @param totalRecaudado total de dinero recaudado por el vendedor
     */
    public ResultadoVendedor(Vendedor vendedor, double totalRecaudado) {
        this.vendedor = vendedor;
        this.totalRecaudado = totalRecaudado;
    }

    /**
     * Obtiene el vendedor.
     *
     * @return el vendedor
     */
    public Vendedor getVendedor() {
        return vendedor;
    }

    /**
     * Obtiene el total recaudado.
     *
     * @return el total de dinero recaudado
     */
    public double getTotalRecaudado() {
        return totalRecaudado;
    }
}
