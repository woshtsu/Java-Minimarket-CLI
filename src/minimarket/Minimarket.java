package minimarket;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets;
import java.io.PrintStream;

public class Minimarket {
    private static final List<Producto> productos = new ArrayList<>();
    private static final List<Cliente> clientes = new ArrayList<>();
    private static final List<Venta> ventas = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static int nextProductoId = 1;
    private static int nextClienteId = 1;
    private static int nextVentaId = 1;
    
    public static void main(String[] args) {
        // Configurar UTF-8 para caracteres especiales
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("console.encoding", "UTF-8");
        } catch (Exception e) {
            // Si hay problemas con UTF-8, continuar sin configuración especial
            System.out.println("Nota: Algunos caracteres especiales podrian no mostrarse correctamente en esta consola.");
        }
        
        inicializarDatosPredeterminados();
        
        int opcion;
        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de linea

            switch (opcion) {
                case 1 -> crearProducto();
                case 2 -> leerProductos();
                case 3 -> actualizarProducto();
                case 4 -> borrarProducto();
                case 5 -> crearCliente();
                case 6 -> leerClientes();
                case 7 -> registrarVenta();
                case 8 -> verVentas();
                case 9 -> System.out.println("Saliendo de la aplicacion. ¡Adios!");
                default -> System.out.println("Opcion no valida. Por favor, intente de nuevo.");
            }
        } while (opcion != 9);
    }
    
    private static void mostrarMenu() {
        System.out.println("\n--- Sistema de Gestion de Minimercado ---");
        System.out.println("1. Crear Producto");
        System.out.println("2. Ver Productos");
        System.out.println("3. Actualizar Producto");
        System.out.println("4. Eliminar Producto");
        System.out.println("5. Crear Cliente");
        System.out.println("6. Ver Clientes");
        System.out.println("7. Registrar Venta");
        System.out.println("8. Ver Ventas");
        System.out.println("9. Salir");
        System.out.print("Elige una opcion: ");
    }

    // Métodos para PRODUCTOS
    private static void crearProducto() {
        System.out.print("Introduce el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Introduce la cantidad en stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        // Seleccionar categoría
        Categoria categoria = seleccionarCategoria();
        if (categoria == null) {
            System.out.println("Categoría no válida. Producto no creado.");
            return;
        }

        Producto nuevoProducto = new Producto(nextProductoId++, nombre, precio, stock, categoria);
        productos.add(nuevoProducto);
        System.out.println("¡Producto creado exitosamente! " + nuevoProducto);
    }

    private static void leerProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
        } else {
            System.out.println("\n--- Inventario de Productos por Categoría ---");
            
            // Agrupar productos por categoría
            for (Categoria categoria : Categoria.values()) {
                List<Producto> productosCategoria = productos.stream()
                    .filter(p -> p.getCategoria() == categoria)
                    .toList();
                
                if (!productosCategoria.isEmpty()) {
                    System.out.println("\n--- " + categoria.getNombre() + " ---");
                    for (Producto producto : productosCategoria) {
                        System.out.println(producto);
                    }
                }
            }
        }
    }

    private static void actualizarProducto() {
        System.out.print("Introduce el ID del producto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        for (Producto producto : productos) {
            if (producto.getId() == id) {
                System.out.print("Introduce el nuevo nombre (actual: " + producto.getNombre() + "): ");
                String nuevoNombre = scanner.nextLine();
                System.out.print("Introduce el nuevo precio (actual: " + producto.getPrecio() + "): ");
                double nuevoPrecio = scanner.nextDouble();
                System.out.print("Introduce la nueva cantidad en stock (actual: " + producto.getStock() + "): ");
                int nuevoStock = scanner.nextInt();
                scanner.nextLine();

                // Actualizar categoría
                System.out.println("Categoría actual: " + producto.getCategoria());
                Categoria nuevaCategoria = seleccionarCategoria();
                if (nuevaCategoria == null) {
                    System.out.println("Categoría no válida. No se actualizará la categoría.");
                    nuevaCategoria = producto.getCategoria();
                }

                producto.setNombre(nuevoNombre);
                producto.setPrecio(nuevoPrecio);
                producto.setStock(nuevoStock);
                producto.setCategoria(nuevaCategoria);
                System.out.println("¡Producto actualizado exitosamente! " + producto);
                return;
            }
        }
        System.out.println("No se encontro ningun producto con el ID: " + id);
    }

    private static void borrarProducto() {
        System.out.print("Introduce el ID del producto a borrar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean borrado = productos.removeIf(producto -> producto.getId() == id);
        if (borrado) {
            System.out.println("¡Producto con ID " + id + " borrado exitosamente!");
        } else {
            System.out.println("No se encontro ningun producto con el ID: " + id);
        }
    }

    // Métodos para CLIENTES
    private static void crearCliente() {
        System.out.print("Introduce el nombre del cliente: ");
        String nombre = scanner.nextLine();

        Cliente nuevoCliente = new Cliente(nextClienteId++, nombre);
        clientes.add(nuevoCliente);
        System.out.println("¡Cliente creado exitosamente! " + nuevoCliente);
    }

    private static void leerClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.println("\n--- Lista de Clientes ---");
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }
        }
    }

    // Métodos para VENTAS
    private static void registrarVenta() {
        System.out.print("Introduce el ID del cliente para la venta: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine();
        
        Cliente clienteEncontrado = clientes.stream()
                .filter(c -> c.getId() == clienteId)
                .findFirst()
                .orElse(null);

        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado. No se puede registrar la venta.");
            return;
        }

        List<Producto> productosSeleccionados = new ArrayList<>();
        String continuar = "s";
        while (continuar.equalsIgnoreCase("s")) {
            // Seleccionar categoría primero
            Categoria categoriaSeleccionada = seleccionarCategoria();
            if (categoriaSeleccionada == null) {
                System.out.println("Categoría no válida. Venta cancelada.");
                return;
            }

            // Mostrar productos de la categoría seleccionada
            mostrarProductosPorCategoria(categoriaSeleccionada);
            System.out.print("Introduce el ID del producto a anadir a la venta: ");
            int productoId = scanner.nextInt();
            scanner.nextLine();

            Producto productoEncontrado = productos.stream()
                    .filter(p -> p.getId() == productoId && p.getCategoria() == categoriaSeleccionada)
                    .findFirst()
                    .orElse(null);

            if (productoEncontrado != null && productoEncontrado.getStock() > 0) {
                productosSeleccionados.add(productoEncontrado);
                productoEncontrado.setStock(productoEncontrado.getStock() - 1);
                System.out.println("Producto añadido: " + productoEncontrado.getNombre());
            } else if (productoEncontrado != null) {
                System.out.println("Producto sin stock.");
            } else {
                System.out.println("Producto no encontrado en esta categoría.");
            }

            System.out.print("¿Quieres anadir otro producto? (s/n): ");
            continuar = scanner.nextLine();
        }

        if (!productosSeleccionados.isEmpty()) {
            Venta nuevaVenta = new Venta(nextVentaId++, clienteEncontrado, productosSeleccionados);
            ventas.add(nuevaVenta);
            System.out.println("¡Venta registrada exitosamente! Monto total: $" + nuevaVenta.getMontoTotal());
            imprimirVoucher(nuevaVenta);
        } else {
            System.out.println("No se anadieron productos. Venta cancelada.");
        }
    }

    private static void verVentas() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
        } else {
            System.out.println("\n--- Historial de Ventas ---");
            for (Venta venta : ventas) {
                System.out.println(venta);
            }
        }
    }

    private static void mostrarProductosPorCategoria(Categoria categoria) {
        List<Producto> productosCategoria = productos.stream()
            .filter(p -> p.getCategoria() == categoria)
            .toList();
        
        if (productosCategoria.isEmpty()) {
            System.out.println("No hay productos en la categoría " + categoria.getNombre());
        } else {
            System.out.println("\n--- Productos de " + categoria.getNombre() + " ---");
            for (Producto producto : productosCategoria) {
                System.out.println(producto);
            }
        }
    }

    private static Categoria seleccionarCategoria() {
        System.out.println("\n--- Seleccionar Categoría ---");
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + ". " + categorias[i].getNombre());
        }
        System.out.print("Elige una categoría (1-" + categorias.length + "): ");
        
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            if (opcion >= 1 && opcion <= categorias.length) {
                return categorias[opcion - 1];
            } else {
                System.out.println("Opción no válida.");
                return null;
            }
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Entrada no válida.");
            return null;
        }
    }

    private static void inicializarDatosPredeterminados() {
        // Inicializar clientes predeterminados
        clientes.add(new Cliente(nextClienteId++, "Ricardo"));
        clientes.add(new Cliente(nextClienteId++, "Carlos"));
        
        // Inicializar productos predeterminados
        // Bebidas
        productos.add(new Producto(nextProductoId++, "Agua Mineral", 1.50, 50, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Coca-Cola", 2.20, 30, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Pepsi", 2.10, 25, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Fanta", 2.00, 20, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Cerveza", 3.50, 40, Categoria.BEBIDAS));
        
        // Bebidas alcohólicas
        productos.add(new Producto(nextProductoId++, "Vino Tinto", 8.50, 15, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Vino Blanco", 8.00, 12, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Whisky", 25.00, 8, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Vodka", 18.50, 10, Categoria.BEBIDAS));
        productos.add(new Producto(nextProductoId++, "Ron", 15.00, 6, Categoria.BEBIDAS));
        
        // Snacks
        productos.add(new Producto(nextProductoId++, "Papas Fritas", 2.50, 30, Categoria.SNACKS));
        productos.add(new Producto(nextProductoId++, "Galletas", 3.00, 25, Categoria.SNACKS));
        productos.add(new Producto(nextProductoId++, "Chocolate", 4.50, 20, Categoria.SNACKS));
        productos.add(new Producto(nextProductoId++, "Dulces", 1.50, 50, Categoria.SNACKS));
        
        // Lácteos
        productos.add(new Producto(nextProductoId++, "Leche", 2.80, 40, Categoria.LACTEOS));
        productos.add(new Producto(nextProductoId++, "Queso", 5.50, 15, Categoria.LACTEOS));
        productos.add(new Producto(nextProductoId++, "Yogurt", 3.20, 30, Categoria.LACTEOS));
        
        // Carnes
        productos.add(new Producto(nextProductoId++, "Pollo", 8.50, 10, Categoria.CARNES));
        productos.add(new Producto(nextProductoId++, "Carne de Res", 12.00, 8, Categoria.CARNES));
        productos.add(new Producto(nextProductoId++, "Pescado", 10.50, 6, Categoria.CARNES));
        
        // Frutas y Verduras
        productos.add(new Producto(nextProductoId++, "Manzanas", 3.50, 25, Categoria.FRUTAS_VERDURAS));
        productos.add(new Producto(nextProductoId++, "Plátanos", 2.00, 30, Categoria.FRUTAS_VERDURAS));
        productos.add(new Producto(nextProductoId++, "Tomates", 4.00, 20, Categoria.FRUTAS_VERDURAS));
        
        // Limpieza
        productos.add(new Producto(nextProductoId++, "Detergente", 6.50, 15, Categoria.LIMPIEZA));
        productos.add(new Producto(nextProductoId++, "Papel Higiénico", 4.00, 20, Categoria.LIMPIEZA));
        productos.add(new Producto(nextProductoId++, "Jabón", 3.50, 25, Categoria.LIMPIEZA));
        
        System.out.println("¡Datos predeterminados cargados exitosamente!");
        System.out.println("Clientes: " + clientes.size() + " | Productos: " + productos.size());
    }

    private static void imprimirVoucher(Venta venta) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaHora = formatter.format(venta.getFecha());
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           VOUCHER DE VENTA");
        System.out.println("=".repeat(50));
        System.out.println("ID de Venta: " + venta.getId());
        System.out.println("Cliente: " + venta.getCliente().getNombre());
        System.out.println("Fecha y Hora: " + fechaHora);
        System.out.println("-".repeat(50));
        System.out.println("PRODUCTOS:");
        
        for (Producto producto : venta.getProductos()) {
            System.out.printf("• %-20s $%.2f%n", producto.getNombre(), producto.getPrecio());
        }
        
        System.out.println("-".repeat(50));
        System.out.printf("TOTAL: $%.2f%n", venta.getMontoTotal());
        System.out.println("=".repeat(50));
        System.out.println("¡Gracias por su compra!");
        System.out.println("=".repeat(50) + "\n");
    }
}
