# Proyecto Ventas POLI — Entrega final (Semanas 7 y 8)

**Módulo:** Conceptos Fundamentales de Programación
**Entrega:** Generación y clasificación de datos
**Grupo:** 14 — Herramientas de Programación Móvil

## Contenido del proyecto

| Entrega | Contenido |
|---|---|
| Entrega 1 (semana 3) | Clase `GenerateInfoFiles`: genera los archivos planos pseudoaleatorios de entrada. |
| Entrega 2 (semana 5) | Versión web con jQuery: [ProyectoVentasPOLI-web](https://github.com/josephbedev/ProyectoVentasPOLI-web). |
| Entrega final (semanas 7 y 8) | Clase `Main` (clase `main` del enunciado): lee los archivos, valida los datos y genera los reportes CSV de vendedores y productos. Incluye pruebas. |

## Reportes que genera la clase `Main`

Se escriben en `data/reportes/`, separados por punto y coma (`;`):

| Archivo | Orden | Formato de cada línea |
|---|---|---|
| `reporte_vendedores.csv` | Dinero recaudado, de mayor a menor | `NombresApellidos;TotalRecaudado` |
| `reporte_productos.csv` | Cantidad vendida, de mayor a menor | `NombreProducto;PrecioPorUnidad` |

El total recaudado de un vendedor es la suma de `precio por unidad × cantidad`
de todas sus ventas. Los vendedores sin ventas aparecen con total `0.00`; en el
reporte de productos solo aparecen los productos que se vendieron al menos una vez.
Además de los archivos, la consola muestra los dos rankings con las unidades vendidas.

## Validaciones

Los datos inválidos no detienen el programa: se omiten y se listan como
advertencias en la consola al final de la ejecución.

- Líneas con campos faltantes o números mal escritos.
- Productos con precio cero o negativo, o con ID repetido.
- Vendedores con documento repetido o campos vacíos.
- Ventas de un producto que no existe en `productos.txt`, o con cantidad cero o negativa.
- Archivos de ventas de un vendedor que no existe en `vendedores.txt`.
- Tipo de documento del archivo de ventas distinto al registrado.
- Si falta `productos.txt`, `vendedores.txt` o la carpeta `ventas`, se informa el error y se indica ejecutar primero `GenerateInfoFiles`.

Si un vendedor tiene varios archivos de ventas, sus ventas se suman.

## Estructura del proyecto

```
ProyectoVentasPOLI/
├── .project
├── .classpath
├── src/
│   └── co/edu/poli/ventas/
│       ├── GenerateInfoFiles.java      (Entrega 1: genera los archivos de prueba)
│       ├── Main.java                   (Entrega final: lee los archivos y genera los reportes)
│       ├── generador/
│       │   └── GeneradorArchivos.java  (métodos de generación de archivos de prueba)
│       ├── lector/
│       │   └── LectorArchivos.java     (lectura y validación de los archivos de entrada)
│       ├── reportes/
│       │   └── GeneradorReportes.java  (cálculos, ordenamiento y escritura de los CSV)
│       └── modelo/
│           ├── Producto.java
│           ├── Vendedor.java
│           ├── LineaVenta.java
│           ├── ResultadoVendedor.java
│           └── ResultadoProducto.java
├── test/
│   └── co/edu/poli/ventas/
│       └── PruebasVentas.java          (pruebas sin librerías externas)
└── data/                                (se genera automáticamente al ejecutar el programa)
    ├── productos.txt
    ├── vendedores.txt
    ├── ventas/
    │   └── ventas_<nombre>_<documento>.txt
    └── reportes/                        (se genera al ejecutar Main)
        ├── reporte_vendedores.csv
        └── reporte_productos.csv
```

## Cómo importar el proyecto en Eclipse

1. Abrir Eclipse para *Java Developers*.
2. `File > Import... > General > Existing Projects into Workspace`.
3. Seleccionar la carpeta `ProyectoVentasPOLI`.
4. Verificar que el proyecto use un JRE compatible con Java 8.

## Cómo ejecutar

1. Ejecutar `co.edu.poli.ventas.GenerateInfoFiles` para crear los archivos de entrada.
2. Ejecutar `co.edu.poli.ventas.Main` para generar los reportes en `data/reportes`.
3. (Opcional) Ejecutar `co.edu.poli.ventas.PruebasVentas` para correr las pruebas.

Las tres se ejecutan con clic derecho → `Run As > Java Application`. Ninguna
solicita datos por consola.

### Detalle de `GenerateInfoFiles`

Ejecutar la clase `co.edu.poli.ventas.GenerateInfoFiles` (clic derecho →
`Run As > Java Application`). El programa no solicita ningún dato por
consola; al finalizar imprime un mensaje de éxito o de error.

Al ejecutarse, se generan dentro de la carpeta `data`:

- `productos.txt`: 15 productos pseudoaleatorios (`ID;Nombre;Precio`).
- `vendedores.txt`: 8 vendedores pseudoaleatorios
  (`TipoDocumento;NúmeroDocumento;Nombres;Apellidos`).
- `data/ventas/`: un archivo de ventas por cada vendedor generado, con el
  tipo y número de documento en la primera línea (el mismo tipo registrado en `vendedores.txt`) y una línea por cada
  venta (`IDProducto;Cantidad;`), referenciando siempre productos que sí
  existen en `productos.txt`.

Las cantidades a generar (número de productos, de vendedores y el rango de
ventas por vendedor) están definidas como constantes al inicio de
`GenerateInfoFiles`, por si se requiere ajustarlas para otras pruebas.

## Métodos de generación implementados (`GeneradorArchivos`)

- `createProductsFile(int productsCount)`
- `createSalesManInfoFile(int salesmanCount)`
- `createSalesMenFile(int randomSalesCount, String name, long id)`

> Nota: como `createSalesMenFile` no recibe el tipo de documento como
> argumento (solo nombre e id), en la entrega final se busca el tipo registrado
> para ese documento en `vendedores.txt`, de modo que ambos archivos coincidan.
> Si el vendedor no se encuentra, se usa `"CC"`.

## Pruebas

`PruebasVentas` crea archivos con datos conocidos en una carpeta temporal y
verifica: el cálculo y orden de ambos reportes, la suma de ventas de un mismo
producto entre vendedores, que los datos inválidos se omitan, que se acumulen
varios archivos de un mismo vendedor, que un vendedor sin ventas quede en cero,
el formato exacto de los CSV y el error cuando falta `productos.txt`.
