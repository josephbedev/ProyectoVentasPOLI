package co.edu.poli.ventas.modelo;

/**
 * Representa la informacion basica de un vendedor de la compania.
 * <p>
 * Corresponde a una linea del archivo de informacion de vendedores, con el
 * formato {@code TipoDocumento;NumeroDocumento;NombresVendedor;ApellidosVendedor}.
 * </p>
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class Vendedor {

    /** Tipo de documento de identificacion (por ejemplo, CC, CE o TI). */
    private final String tipoDocumento;

    /** Numero del documento de identificacion. */
    private final long numeroDocumento;

    /** Nombres del vendedor. */
    private final String nombres;

    /** Apellidos del vendedor. */
    private final String apellidos;

    /**
     * Crea un nuevo vendedor con los datos indicados.
     *
     * @param tipoDocumento   tipo de documento de identificacion
     * @param numeroDocumento numero del documento de identificacion
     * @param nombres         nombres del vendedor
     * @param apellidos       apellidos del vendedor
     */
    public Vendedor(String tipoDocumento, long numeroDocumento, String nombres, String apellidos) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el tipo de documento del vendedor.
     *
     * @return el tipo de documento (CC, CE, TI, entre otros)
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Obtiene el numero de documento del vendedor.
     *
     * @return el numero del documento de identificacion
     */
    public long getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * Obtiene los nombres del vendedor.
     *
     * @return los nombres del vendedor
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Obtiene los apellidos del vendedor.
     *
     * @return los apellidos del vendedor
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Obtiene el nombre completo del vendedor, resultado de concatenar sus
     * nombres y apellidos separados por un espacio.
     *
     * @return el nombre completo del vendedor
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Construye la representacion en linea plana del vendedor, lista para
     * escribirse en el archivo de vendedores, con el formato
     * {@code TipoDocumento;NumeroDocumento;NombresVendedor;ApellidosVendedor}.
     *
     * @return la linea de texto plano correspondiente a este vendedor
     */
    public String toLineaArchivo() {
        return tipoDocumento + ";" + numeroDocumento + ";" + nombres + ";" + apellidos;
    }
}
