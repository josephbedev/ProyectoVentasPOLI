package co.edu.poli.ventas.lector;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.poli.ventas.modelo.LineaVenta;
import co.edu.poli.ventas.modelo.Producto;
import co.edu.poli.ventas.modelo.Vendedor;

/**
 * Lee y valida los archivos planos de entrada del proyecto: el archivo de
 * productos, el archivo de vendedores y todos los archivos de ventas que se
 * encuentren en la carpeta de ventas.
 * <p>
 * Las lineas con informacion invalida (campos faltantes, numeros mal
 * escritos, precios o cantidades negativas, productos o vendedores que no
 * existen, identificadores duplicados) no detienen el programa: se omiten y
 * se registran como advertencias, que pueden consultarse con
 * {@link #getAdvertencias()}.
 * </p>
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public class LectorArchivos {

    /** Nombre del archivo de productos dentro de la carpeta de datos. */
    public static final String ARCHIVO_PRODUCTOS = "productos.txt";

    /** Nombre del archivo de vendedores dentro de la carpeta de datos. */
    public static final String ARCHIVO_VENDEDORES = "vendedores.txt";

    /** Nombre de la subcarpeta que agrupa los archivos de ventas. */
    public static final String CARPETA_VENTAS = "ventas";

    /** Carpeta raiz donde se encuentran los archivos de entrada. */
    private final Path carpetaDatos;

    /** Advertencias encontradas durante la lectura. */
    private final List<String> advertencias = new ArrayList<>();

    /**
     * Crea un lector que toma los archivos de la carpeta indicada.
     *
     * @param carpetaDatos carpeta raiz donde estan los archivos de entrada
     */
    public LectorArchivos(Path carpetaDatos) {
        this.carpetaDatos = carpetaDatos;
    }

    /**
     * Lee el archivo de productos ({@code IDProducto;NombreProducto;PrecioPorUnidad}).
     *
     * @return los productos validos, indexados por su identificador y en el
     *         orden en que aparecen en el archivo
     * @throws IOException si el archivo no existe o no puede leerse
     */
    public Map<Integer, Producto> leerProductos() throws IOException {
        Map<Integer, Producto> productos = new LinkedHashMap<>();
        List<String> lineas = leerLineasObligatorias(carpetaDatos.resolve(ARCHIVO_PRODUCTOS));

        for (int i = 0; i < lineas.size(); i++) {
            String ubicacion = ARCHIVO_PRODUCTOS + ", linea " + (i + 1);
            String[] campos = lineas.get(i).split(";");
            if (campos.length < 3) {
                advertir(ubicacion, "se esperaban 3 campos (ID;Nombre;Precio)");
                continue;
            }
            try {
                int id = Integer.parseInt(campos[0].trim());
                String nombre = campos[1].trim();
                double precio = Double.parseDouble(campos[2].trim());
                if (nombre.isEmpty()) {
                    advertir(ubicacion, "el nombre del producto esta vacio");
                } else if (precio <= 0) {
                    advertir(ubicacion, "el precio debe ser mayor a cero (" + precio + ")");
                } else if (productos.containsKey(id)) {
                    advertir(ubicacion, "el ID de producto " + id + " esta repetido");
                } else {
                    productos.put(id, new Producto(id, nombre, precio));
                }
            } catch (NumberFormatException e) {
                advertir(ubicacion, "el ID o el precio no es un numero valido");
            }
        }
        return productos;
    }

    /**
     * Lee el archivo de vendedores
     * ({@code TipoDocumento;NumeroDocumento;Nombres;Apellidos}).
     *
     * @return los vendedores validos, indexados por su numero de documento y
     *         en el orden en que aparecen en el archivo
     * @throws IOException si el archivo no existe o no puede leerse
     */
    public Map<Long, Vendedor> leerVendedores() throws IOException {
        Map<Long, Vendedor> vendedores = new LinkedHashMap<>();
        List<String> lineas = leerLineasObligatorias(carpetaDatos.resolve(ARCHIVO_VENDEDORES));

        for (int i = 0; i < lineas.size(); i++) {
            String ubicacion = ARCHIVO_VENDEDORES + ", linea " + (i + 1);
            String[] campos = lineas.get(i).split(";");
            if (campos.length < 4) {
                advertir(ubicacion, "se esperaban 4 campos (Tipo;Documento;Nombres;Apellidos)");
                continue;
            }
            try {
                String tipo = campos[0].trim();
                long documento = Long.parseLong(campos[1].trim());
                String nombres = campos[2].trim();
                String apellidos = campos[3].trim();
                if (tipo.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
                    advertir(ubicacion, "hay campos vacios");
                } else if (documento <= 0) {
                    advertir(ubicacion, "el numero de documento debe ser positivo");
                } else if (vendedores.containsKey(documento)) {
                    advertir(ubicacion, "el documento " + documento + " esta repetido");
                } else {
                    vendedores.put(documento, new Vendedor(tipo, documento, nombres, apellidos));
                }
            } catch (NumberFormatException e) {
                advertir(ubicacion, "el numero de documento no es valido");
            }
        }
        return vendedores;
    }

    /**
     * Lee todos los archivos {@code .txt} de la carpeta de ventas. Cada
     * archivo tiene en su primera linea {@code TipoDocumento;NumeroDocumento}
     * y luego una linea por venta con {@code IDProducto;Cantidad;}.
     * <p>
     * Si un mismo vendedor tiene varios archivos de ventas, sus ventas se
     * acumulan. Se omiten (con advertencia) los archivos de vendedores que
     * no existen y las lineas con productos inexistentes o cantidades no
     * positivas.
     * </p>
     *
     * @param productos  productos validos, para verificar los IDs vendidos
     * @param vendedores vendedores validos, para verificar los documentos
     * @return las lineas de venta validas, agrupadas por documento de vendedor
     * @throws IOException si la carpeta de ventas no existe o no puede leerse
     */
    public Map<Long, List<LineaVenta>> leerVentas(Map<Integer, Producto> productos,
            Map<Long, Vendedor> vendedores) throws IOException {
        Path carpetaVentas = carpetaDatos.resolve(CARPETA_VENTAS);
        if (!Files.isDirectory(carpetaVentas)) {
            throw new IOException("No existe la carpeta de ventas: " + carpetaVentas.toAbsolutePath());
        }

        List<Path> archivos = new ArrayList<>();
        try (DirectoryStream<Path> flujo = Files.newDirectoryStream(carpetaVentas, "*.txt")) {
            for (Path archivo : flujo) {
                archivos.add(archivo);
            }
        }
        Collections.sort(archivos);

        Map<Long, List<LineaVenta>> ventas = new LinkedHashMap<>();
        for (Path archivo : archivos) {
            leerArchivoVentas(archivo, productos, vendedores, ventas);
        }
        return ventas;
    }

    /**
     * Lee un unico archivo de ventas y acumula sus lineas validas.
     */
    private void leerArchivoVentas(Path archivo, Map<Integer, Producto> productos,
            Map<Long, Vendedor> vendedores, Map<Long, List<LineaVenta>> ventas) throws IOException {
        String nombreArchivo = archivo.getFileName().toString();
        List<String> lineas = leerLineas(archivo);
        if (lineas.isEmpty()) {
            advertir(nombreArchivo, "el archivo esta vacio");
            return;
        }

        String[] encabezado = lineas.get(0).split(";");
        long documento;
        try {
            if (encabezado.length < 2) {
                throw new NumberFormatException();
            }
            documento = Long.parseLong(encabezado[1].trim());
        } catch (NumberFormatException e) {
            advertir(nombreArchivo, "la primera linea debe ser TipoDocumento;NumeroDocumento");
            return;
        }
        Vendedor vendedor = vendedores.get(documento);
        if (vendedor == null) {
            advertir(nombreArchivo, "el vendedor con documento " + documento + " no existe en "
                    + ARCHIVO_VENDEDORES + "; se omite el archivo");
            return;
        }
        if (!vendedor.getTipoDocumento().equalsIgnoreCase(encabezado[0].trim())) {
            advertir(nombreArchivo, "el tipo de documento (" + encabezado[0].trim()
                    + ") no coincide con el registrado (" + vendedor.getTipoDocumento() + ")");
        }

        List<LineaVenta> lineasVendedor = ventas.get(documento);
        if (lineasVendedor == null) {
            lineasVendedor = new ArrayList<>();
            ventas.put(documento, lineasVendedor);
        }

        for (int i = 1; i < lineas.size(); i++) {
            String ubicacion = nombreArchivo + ", linea " + (i + 1);
            String[] campos = lineas.get(i).split(";");
            if (campos.length < 2) {
                advertir(ubicacion, "se esperaba IDProducto;Cantidad;");
                continue;
            }
            try {
                int idProducto = Integer.parseInt(campos[0].trim());
                int cantidad = Integer.parseInt(campos[1].trim());
                if (!productos.containsKey(idProducto)) {
                    advertir(ubicacion, "el producto " + idProducto + " no existe");
                } else if (cantidad <= 0) {
                    advertir(ubicacion, "la cantidad debe ser mayor a cero (" + cantidad + ")");
                } else {
                    lineasVendedor.add(new LineaVenta(idProducto, cantidad));
                }
            } catch (NumberFormatException e) {
                advertir(ubicacion, "el ID o la cantidad no es un numero valido");
            }
        }
    }

    /**
     * Obtiene las advertencias registradas durante la lectura.
     *
     * @return lista de advertencias, en el orden en que se encontraron
     */
    public List<String> getAdvertencias() {
        return Collections.unmodifiableList(advertencias);
    }

    private List<String> leerLineasObligatorias(Path archivo) throws IOException {
        if (!Files.exists(archivo)) {
            throw new IOException("No se encontro el archivo " + archivo.toAbsolutePath()
                    + ". Ejecute primero GenerateInfoFiles.");
        }
        return leerLineas(archivo);
    }

    /** Lee las lineas no vacias de un archivo en UTF-8. */
    private static List<String> leerLineas(Path archivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        }
        return lineas;
    }

    private void advertir(String ubicacion, String mensaje) {
        advertencias.add(ubicacion + ": " + mensaje);
    }
}
