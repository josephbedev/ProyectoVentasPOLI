package co.edu.poli.ventas.generador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.edu.poli.ventas.modelo.Producto;
import co.edu.poli.ventas.modelo.Vendedor;

/**
 * Clase utilitaria con los metodos de generacion de archivos planos
 * pseudoaleatorios que sirven como datos de prueba para el proyecto de
 * ventas: archivo de productos, archivo de informacion de vendedores y
 * archivos individuales de ventas por vendedor.
 * <p>
 * Todos los archivos generados se ubican dentro de la carpeta {@code data}
 * del proyecto: el archivo de productos y el de vendedores directamente en
 * {@code data}, y los archivos de ventas de cada vendedor en la subcarpeta
 * {@code data/ventas}, de manera que todos los archivos de vendedores queden
 * agrupados en una misma carpeta, tal como lo exige el enunciado.
 * </p>
 * <p>
 * Esta clase no debe instanciarse: todos sus metodos son estaticos.
 * </p>
 *
 * @author Grupo 14 - Herramientas de Programacion Movil
 */
public final class GeneradorArchivos {

    /** Carpeta raiz donde se guardan los archivos de datos del proyecto. */
    private static final String CARPETA_DATOS = "data";

    /** Carpeta donde se guarda un archivo de ventas por cada vendedor. */
    private static final String CARPETA_VENTAS = CARPETA_DATOS + File.separator + "ventas";

    /** Nombre del archivo de productos. */
    private static final String ARCHIVO_PRODUCTOS = CARPETA_DATOS + File.separator + "productos.txt";

    /** Nombre del archivo de informacion de vendedores. */
    private static final String ARCHIVO_VENDEDORES = CARPETA_DATOS + File.separator + "vendedores.txt";

    /** Generador de numeros pseudoaleatorios reutilizado por toda la clase. */
    private static final Random ALEATORIO = new Random();

    /** Tipos de documento de identificacion validos para los vendedores. */
    private static final String[] TIPOS_DOCUMENTO = { "CC", "CE", "TI" };

    /** Banco de nombres reales usado para generar vendedores coherentes. */
    private static final String[] NOMBRES = {
            "Maria", "Carlos", "Laura", "Andres", "Camila", "Juan", "Valentina",
            "Santiago", "Daniela", "Felipe", "Sofia", "Diego", "Paula", "David",
            "Natalia", "Sebastian", "Mariana", "Alejandro", "Gabriela", "Julian"
    };

    /** Banco de apellidos reales usado para generar vendedores coherentes. */
    private static final String[] APELLIDOS = {
            "Gomez", "Rodriguez", "Martinez", "Lopez", "Garcia", "Perez",
            "Sanchez", "Ramirez", "Torres", "Diaz", "Vargas", "Castro",
            "Rojas", "Morales", "Suarez", "Ortiz", "Jimenez", "Herrera"
    };

    /** Banco de nombres genericos de productos usado para generar el catalogo. */
    private static final String[] NOMBRES_PRODUCTOS = {
            "Arroz", "Aceite", "Pasta", "Azucar", "Sal", "Cafe", "Chocolate",
            "Harina", "Lenteja", "Frijol", "Atun", "Leche", "Jabon", "Detergente",
            "Papel higienico", "Shampoo", "Crema dental", "Avena", "Galletas", "Gaseosa"
    };

    /**
     * Constructor privado: esta clase solo expone metodos estaticos y no debe
     * instanciarse.
     */
    private GeneradorArchivos() {
    }

    /**
     * Genera el archivo de productos de la compania, con informacion
     * pseudoaleatoria, en el formato
     * {@code IDProducto;NombreProducto;PrecioPorUnidadProducto}, un producto
     * por linea.
     *
     * @param productsCount cantidad de productos a generar; debe ser mayor a cero
     * @return la lista de los productos generados, en el mismo orden en que
     *         quedaron escritos en el archivo
     * @throws IOException              si ocurre un error al crear la carpeta
     *                                   de datos o al escribir el archivo
     * @throws IllegalArgumentException si {@code productsCount} no es mayor a cero
     */
    public static List<Producto> createProductsFile(int productsCount) throws IOException {
        if (productsCount <= 0) {
            throw new IllegalArgumentException("La cantidad de productos debe ser mayor a cero.");
        }

        crearCarpetaSiNoExiste(CARPETA_DATOS);

        List<Producto> productos = new ArrayList<>();
        Path rutaArchivo = Paths.get(ARCHIVO_PRODUCTOS);

        try (BufferedWriter escritor = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
            for (int idProducto = 1; idProducto <= productsCount; idProducto++) {
                String nombreBase = NOMBRES_PRODUCTOS[ALEATORIO.nextInt(NOMBRES_PRODUCTOS.length)];
                String nombreProducto = nombreBase + " " + generarReferencia();
                double precioPorUnidad = generarPrecioPseudoaleatorio();

                Producto producto = new Producto(idProducto, nombreProducto, precioPorUnidad);
                productos.add(producto);

                escritor.write(producto.toLineaArchivo());
                escritor.newLine();
            }
        }

        return productos;
    }

    /**
     * Genera el archivo con la informacion de los vendedores de la compania,
     * con datos pseudoaleatorios pero coherentes (nombres y apellidos reales),
     * en el formato
     * {@code TipoDocumento;NumeroDocumento;NombresVendedor;ApellidosVendedor},
     * un vendedor por linea.
     *
     * @param salesmanCount cantidad de vendedores a generar; debe ser mayor a cero
     * @return la lista de los vendedores generados, en el mismo orden en que
     *         quedaron escritos en el archivo
     * @throws IOException              si ocurre un error al crear la carpeta
     *                                   de datos o al escribir el archivo
     * @throws IllegalArgumentException si {@code salesmanCount} no es mayor a cero
     */
    public static List<Vendedor> createSalesManInfoFile(int salesmanCount) throws IOException {
        if (salesmanCount <= 0) {
            throw new IllegalArgumentException("La cantidad de vendedores debe ser mayor a cero.");
        }

        crearCarpetaSiNoExiste(CARPETA_DATOS);

        List<Vendedor> vendedores = new ArrayList<>();
        Path rutaArchivo = Paths.get(ARCHIVO_VENDEDORES);

        try (BufferedWriter escritor = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDocumento = TIPOS_DOCUMENTO[ALEATORIO.nextInt(TIPOS_DOCUMENTO.length)];
                long numeroDocumento = generarNumeroDocumentoPseudoaleatorio();
                String nombres = NOMBRES[ALEATORIO.nextInt(NOMBRES.length)];
                String apellidos = APELLIDOS[ALEATORIO.nextInt(APELLIDOS.length)]
                        + " " + APELLIDOS[ALEATORIO.nextInt(APELLIDOS.length)];

                Vendedor vendedor = new Vendedor(tipoDocumento, numeroDocumento, nombres, apellidos);
                vendedores.add(vendedor);

                escritor.write(vendedor.toLineaArchivo());
                escritor.newLine();
            }
        }

        return vendedores;
    }

    /**
     * Genera el archivo pseudoaleatorio de ventas de un unico vendedor,
     * identificado por su nombre y su numero de documento. El archivo queda
     * ubicado en la carpeta {@code data/ventas} y su contenido sigue el
     * formato definido en el enunciado: la primera linea con
     * {@code TipoDocumentoVendedor;NumeroDocumentoVendedor} y, a
     * continuacion, una linea por cada venta con el formato
     * {@code IDProducto;CantidadVendida;}.
     * <p>
     * Para garantizar que los identificadores de producto referenciados sean
     * coherentes, este metodo lee los productos ya existentes en
     * {@code data/productos.txt}; por lo tanto, {@link #createProductsFile(int)}
     * debe haberse ejecutado previamente.
     * </p>
     * <p>
     * Como el tipo de documento no forma parte de los argumentos de este
     * metodo, se busca en {@code data/vendedores.txt} el vendedor con ese
     * numero de documento y se usa su tipo registrado, para que ambos
     * archivos sean coherentes. Si no se encuentra, se usa {@code "CC"}.
     * </p>
     *
     * @param randomSalesCount cantidad de ventas pseudoaleatorias a generar;
     *                         debe ser mayor a cero
     * @param name             nombre completo del vendedor, usado para nombrar el archivo
     * @param id               numero de documento del vendedor
     * @throws IOException              si no existe el archivo de productos, o si
     *                                   ocurre un error al crear la carpeta de ventas
     *                                   o al escribir el archivo
     * @throws IllegalArgumentException si {@code randomSalesCount} no es mayor a cero
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        if (randomSalesCount <= 0) {
            throw new IllegalArgumentException("La cantidad de ventas debe ser mayor a cero.");
        }

        List<Producto> productosDisponibles = leerProductosDelArchivo();
        if (productosDisponibles.isEmpty()) {
            throw new IOException("No se encontraron productos en " + ARCHIVO_PRODUCTOS
                    + ". Ejecute createProductsFile(int) antes de generar ventas.");
        }

        crearCarpetaSiNoExiste(CARPETA_VENTAS);

        String tipoDocumento = buscarTipoDocumento(id);
        String nombreArchivo = "ventas_" + normalizarNombreArchivo(name) + "_" + id + ".txt";
        Path rutaArchivo = Paths.get(CARPETA_VENTAS, nombreArchivo);

        try (BufferedWriter escritor = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
            escritor.write(tipoDocumento + ";" + id);
            escritor.newLine();

            for (int i = 0; i < randomSalesCount; i++) {
                Producto productoVendido = productosDisponibles.get(ALEATORIO.nextInt(productosDisponibles.size()));
                int cantidadVendida = 1 + ALEATORIO.nextInt(20);

                escritor.write(productoVendido.getId() + ";" + cantidadVendida + ";");
                escritor.newLine();
            }
        }
    }

    /**
     * Lee el archivo de productos previamente generado y lo convierte en una
     * lista de objetos {@link Producto}.
     *
     * @return la lista de productos leidos del archivo, o una lista vacia si
     *         el archivo aun no existe
     * @throws IOException si ocurre un error de lectura distinto a la
     *                      ausencia del archivo
     */
    private static List<Producto> leerProductosDelArchivo() throws IOException {
        List<Producto> productos = new ArrayList<>();
        Path rutaArchivo = Paths.get(ARCHIVO_PRODUCTOS);

        if (!Files.exists(rutaArchivo)) {
            return productos;
        }

        try (BufferedReader lector = Files.newBufferedReader(rutaArchivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linea.split(";");
                int idProducto = Integer.parseInt(campos[0].trim());
                String nombreProducto = campos[1].trim();
                double precioPorUnidad = Double.parseDouble(campos[2].trim());
                productos.add(new Producto(idProducto, nombreProducto, precioPorUnidad));
            }
        }

        return productos;
    }

    /**
     * Busca en el archivo de vendedores el tipo de documento registrado para
     * el numero de documento indicado.
     *
     * @param numeroDocumento numero de documento del vendedor
     * @return el tipo de documento registrado, o {@code "CC"} si el archivo
     *         no existe o el vendedor no se encuentra
     * @throws IOException si ocurre un error de lectura
     */
    private static String buscarTipoDocumento(long numeroDocumento) throws IOException {
        Path rutaArchivo = Paths.get(ARCHIVO_VENDEDORES);
        if (Files.exists(rutaArchivo)) {
            try (BufferedReader lector = Files.newBufferedReader(rutaArchivo, StandardCharsets.UTF_8)) {
                String linea;
                while ((linea = lector.readLine()) != null) {
                    String[] campos = linea.split(";");
                    if (campos.length >= 2 && campos[1].trim().equals(String.valueOf(numeroDocumento))) {
                        return campos[0].trim();
                    }
                }
            }
        }
        return "CC";
    }

    /**
     * Crea la carpeta indicada si todavia no existe, incluyendo las carpetas
     * padre necesarias.
     *
     * @param rutaCarpeta ruta relativa de la carpeta a crear
     * @throws IOException si ocurre un error al crear la carpeta
     */
    private static void crearCarpetaSiNoExiste(String rutaCarpeta) throws IOException {
        Files.createDirectories(Paths.get(rutaCarpeta));
    }

    /**
     * Genera un precio por unidad pseudoaleatorio, con dos decimales, entre
     * 1000 y 100000 pesos colombianos.
     *
     * @return el precio por unidad generado
     */
    private static double generarPrecioPseudoaleatorio() {
        double precio = 1000 + ALEATORIO.nextDouble() * 99000;
        return Math.round(precio * 100.0) / 100.0;
    }

    /**
     * Genera un numero de documento pseudoaleatorio de ocho digitos.
     *
     * @return el numero de documento generado
     */
    private static long generarNumeroDocumentoPseudoaleatorio() {
        return 10_000_000L + ALEATORIO.nextInt(90_000_000);
    }

    /**
     * Genera una referencia corta pseudoaleatoria, usada para diferenciar
     * productos que comparten el mismo nombre base.
     *
     * @return la referencia generada, con el formato {@code REF-nnn}
     */
    private static String generarReferencia() {
        int numero = 100 + ALEATORIO.nextInt(900);
        return "REF-" + numero;
    }

    /**
     * Normaliza un nombre para usarlo como parte de un nombre de archivo,
     * remplazando espacios por guiones bajos y eliminando caracteres que no
     * sean letras, numeros o guiones bajos.
     *
     * @param nombre nombre original a normalizar
     * @return el nombre normalizado, valido para un nombre de archivo
     */
    private static String normalizarNombreArchivo(String nombre) {
        String sinEspacios = nombre.trim().replaceAll("\\s+", "_");
        return sinEspacios.replaceAll("[^A-Za-z0-9_]", "");
    }
}
