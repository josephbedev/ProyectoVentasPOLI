# Proyecto Ventas POLI — Entrega 1 (Semana 3)

**Módulo:** Conceptos Fundamentales de Programación
**Entrega:** Generación y clasificación de datos
**Grupo:** 14 — Herramientas de Programación Móvil

## Contenido de esta entrega

Esta primera entrega corresponde al diseño e implementación de la clase
`GenerateInfoFiles`, encargada de generar los archivos planos
pseudoaleatorios que servirán como entrada para el programa principal
(clase `main`, que se implementará en las siguientes entregas).

## Estructura del proyecto

```
ProyectoVentasPOLI/
├── .project
├── .classpath
├── src/
│   └── co/edu/poli/ventas/
│       ├── GenerateInfoFiles.java      (clase con el método main de esta entrega)
│       ├── generador/
│       │   └── GeneradorArchivos.java  (métodos de generación de archivos de prueba)
│       └── modelo/
│           ├── Producto.java
│           └── Vendedor.java
└── data/                                (se genera automáticamente al ejecutar el programa)
    ├── productos.txt
    ├── vendedores.txt
    └── ventas/
        └── ventas_<nombre>_<documento>.txt
```

## Cómo importar el proyecto en Eclipse

1. Abrir Eclipse para *Java Developers*.
2. `File > Import... > General > Existing Projects into Workspace`.
3. Seleccionar la carpeta `ProyectoVentasPOLI`.
4. Verificar que el proyecto use un JRE compatible con Java 8.

## Cómo ejecutar

Ejecutar la clase `co.edu.poli.ventas.GenerateInfoFiles` (clic derecho →
`Run As > Java Application`). El programa no solicita ningún dato por
consola; al finalizar imprime un mensaje de éxito o de error.

Al ejecutarse, se generan dentro de la carpeta `data`:

- `productos.txt`: 15 productos pseudoaleatorios (`ID;Nombre;Precio`).
- `vendedores.txt`: 8 vendedores pseudoaleatorios
  (`TipoDocumento;NúmeroDocumento;Nombres;Apellidos`).
- `data/ventas/`: un archivo de ventas por cada vendedor generado, con el
  tipo y número de documento en la primera línea y una línea por cada
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
> argumento (solo nombre e id), se asigna por defecto el tipo `"CC"` para
> el archivo de ventas individual. Este supuesto puede ajustarse fácilmente
> si el docente indica un criterio distinto.
