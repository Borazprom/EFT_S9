
package com.mycompany.teatro_moro;

import java.util.*;

public class TeatroMoro {

    private Asiento[] asientos;
    private List<Cliente> clientes = new ArrayList<>();
    private List<Venta> ventas = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private List<Descuento> descuentos = new ArrayList<>();

    private int idVenta = 1;
    private int idReserva = 1;
    private int totalIngresos = 0;
    private final String[] ubicaciones = {"VIP", "Palco", "Platea Baja", "Platea Alta", "Galería"};

    public TeatroMoro() {
        // Inicializar asientos con ubicación y precio variable
        asientos = new Asiento[5 * 4]; // 5 ubicaciones, 4 asientos por ubicación
        int asientoIndex = 0;
        int precioBase;
        for (String ubicacion : ubicaciones) {
            switch (ubicacion) {
                case "VIP":
                    precioBase = 10000;
                    break;
                case "Palco":
                    precioBase = 8000;
                    break;
                case "Platea Baja":
                    precioBase = 7000;
                    break;
                case "Platea Alta":
                    precioBase = 6000;
                    break;
                case "Galería":
                    precioBase = 4000;
                    break;
                default:
                    precioBase = 5000;
                    break;
            }
            for (int i = 1; i <= 4; i++) {
                asientos[asientoIndex++] = new Asiento(asientoIndex, precioBase, ubicacion);
            }
        }

        descuentos.add(new Descuento("Niño", 0.10));
        descuentos.add(new Descuento("Mujer", 0.20));
        descuentos.add(new Descuento("Estudiante", 0.15));
        descuentos.add(new Descuento("Tercera Edad", 0.25));
    }

    public void mostrarMenu() {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n---------------------------------");
            System.out.println("\n--- Bienvenido al Teatro Moro ---");
            System.out.println("\n---------------------------------");     
            System.out.println("1. Reservar entrada");
            System.out.println("2. Comprar entrada");
            System.out.println("3. Modificar venta");
            System.out.println("4. Imprimir boleta");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1 -> reservarEntrada(sc);
                case 2 -> comprarEntrada(sc);
                case 3 -> modificarVenta(sc);
                case 4 -> imprimirBoleta(sc);
                case 5 -> System.out.println("Gracias por venir a Teatro Moro.");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    private Cliente registrarCliente(Scanner sc) {
        System.out.print("Ingrese su nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Ingrese su edad: ");
        int edad = sc.nextInt();
        sc.nextLine(); 

        String tipo = "Normal";
        if (edad <= 12) {
            tipo = "Niño";
        } else if (edad < 18) {
            System.out.print("¿Es estudiante? (si/no): ");
            String esEstudiante = sc.nextLine().toLowerCase();
            if (esEstudiante.equals("si")) {
                tipo = "Estudiante";
            }
        } else if (edad >= 60) {
            tipo = "Tercera Edad";
        } else {
            System.out.print("¿Es mujer? (si/no): ");
            String esMujer = sc.nextLine().toLowerCase();
            if (esMujer.equals("si")) {
                tipo = "Mujer";
            }
        }

        Cliente cliente = new Cliente(nombre, edad, tipo);
        clientes.add(cliente);
        return cliente;
    }

    private double aplicarDescuento(Cliente cliente, int precioBase) {
        for (Descuento d : descuentos) {
            if (d.getTipoCliente().equalsIgnoreCase(cliente.getTipo())) {
                return precioBase * (1 - d.getPorcentaje());
            }
        }
        return precioBase;
    }

    private void mostrarAsientosDisponibles(String ubicacion) {
        System.out.println("\n--- Asientos Disponibles en " + ubicacion + " ---");
        boolean hayDisponibles = false;
        for (Asiento a : asientos) {
            if (a.getUbicacion().equalsIgnoreCase(ubicacion) && !a.isVendido() && !a.isReservado()) {
                System.out.println("Asiento " + a.getNumero() + " - $" + a.getPrecio());
                hayDisponibles = true;
            }
        }
        if (!hayDisponibles) {
            System.out.println("No hay asientos disponibles en esta ubicación.");
        }
    }

    private Asiento seleccionarAsiento(Scanner sc, String ubicacion) {
        mostrarAsientosDisponibles(ubicacion);
        System.out.print("Seleccione el número de asiento en " + ubicacion + ": ");
        int numAsiento = sc.nextInt();
        sc.nextLine(); 

        for (Asiento a : asientos) {
            if (a.getUbicacion().equalsIgnoreCase(ubicacion) && a.getNumero() == numAsiento && 
                    !a.isVendido() && !a.isReservado()) {
                return a;
            }
        }
        System.out.println("Asiento inválido o no disponible en " + ubicacion + ".");
        return null;
    }

    private void reservarEntrada(Scanner sc) {
        System.out.println("\n--- Reserva de Entrada ---");
        System.out.println("Ubicaciones disponibles: " + Arrays.toString(ubicaciones));
        System.out.print("Seleccione la ubicación deseada: ");
        String ubicacionSeleccionada = sc.nextLine();

        Asiento asientoSeleccionado = seleccionarAsiento(sc, ubicacionSeleccionada);
        if (asientoSeleccionado == null) {
            return;
        }

        Cliente cliente = registrarCliente(sc);
        asientoSeleccionado.setReservado(true);
        reservas.add(new Reserva(idReserva++, cliente, asientoSeleccionado));
        System.out.println("Reserva realizada exitosamente para el asiento " 
                + asientoSeleccionado.getNumero() + " en " + asientoSeleccionado.getUbicacion() + ".");
    }

    private void comprarEntrada(Scanner sc) {
        System.out.println("\n--- Compra de Entrada ---");
        System.out.println("Ubicaciones disponibles: " + Arrays.toString(ubicaciones));
        System.out.print("Seleccione la ubicación deseada: ");
        String ubicacionSeleccionada = sc.nextLine();

        Asiento asientoSeleccionado = seleccionarAsiento(sc, ubicacionSeleccionada);
        if (asientoSeleccionado == null) {
            return;
        }

        if (asientoSeleccionado.isVendido()) {
            System.out.println("El Asiento ya fue vendido.");
            return;
        }

        Cliente cliente = registrarCliente(sc);
        double precioFinal = aplicarDescuento(cliente, asientoSeleccionado.getPrecio());

        asientoSeleccionado.setVendido(true);
        asientoSeleccionado.setReservado(false);
        reservas.removeIf(r -> r.getAsiento().getNumero() == asientoSeleccionado.getNumero() 
                && r.getAsiento().getUbicacion().equalsIgnoreCase(asientoSeleccionado.getUbicacion()));

        Venta nuevaVenta = new Venta(idVenta++, cliente, asientoSeleccionado, precioFinal);
        ventas.add(new Venta(idVenta++, cliente, asientoSeleccionado, precioFinal));
        totalIngresos += precioFinal;

       System.out.println("Compra exitosa. ID de Venta: " + nuevaVenta.getId()); 
        System.out.println("Asiento: " + asientoSeleccionado.getNumero() + " en " + asientoSeleccionado.getUbicacion() + ", Precio final: $" + (int) precioFinal);
    }

    private void modificarVenta(Scanner sc) {
        System.out.print("Ingrese ID de venta a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Venta ventaAModificar = null;
        for (Venta v : ventas) {
            if (v.getId() == id) {
                ventaAModificar = v;
                break;
            }
        }

        if (ventaAModificar == null) {
            System.out.println("Venta no encontrada.");
            return;
        }

        System.out.println("\n--- Modificar Venta ---");
        System.out.println("Ubicaciones disponibles: " + Arrays.toString(ubicaciones));
        System.out.print("Seleccione la nueva ubicación deseada: ");
        String nuevaUbicacion = sc.nextLine();

        Asiento nuevoAsiento = seleccionarAsiento(sc, nuevaUbicacion);
        if (nuevoAsiento == null) {
            return;
        }

        if (nuevoAsiento.isVendido()) {
            System.out.println("Este asiento ya fue vendido.");
            return;
        }

        // Desmarca el asiento anterior como vendido
        ventaAModificar.getAsiento().setVendido(false);
        totalIngresos -= ventaAModificar.getPrecioPagado();

        // Asigna el nuevo asiento
        nuevoAsiento.setVendido(true);
        double nuevoPrecio = aplicarDescuento(ventaAModificar.getCliente(), nuevoAsiento.getPrecio());
        Venta nuevaVenta = new Venta(ventaAModificar.getId(), ventaAModificar.getCliente(), nuevoAsiento, nuevoPrecio);

        // Actualiza la lista de ventas
        ventas.removeIf(v -> v.getId() == id);
        ventas.add(nuevaVenta);
        totalIngresos += nuevoPrecio;

        System.out.println("Venta modificada correctamente. Nuevo asiento: " + nuevoAsiento.getNumero() + " en " + nuevoAsiento.getUbicacion() + ", Nuevo precio: $" + (int) nuevoPrecio);
    }

    private void imprimirBoleta(Scanner sc) {
        System.out.print("Ingrese ID de venta: ");
        int id = sc.nextInt();
        sc.nextLine(); 

        for (Venta v : ventas) {
            if (v.getId() == id) {
                System.out.println("\n--- Boleta ---");
                System.out.println("ID de Venta: " + v.getId());
                System.out.println("Cliente: " + v.getCliente().getNombre());
                System.out.println("Edad: " + v.getCliente().getEdad());
                System.out.println("Tipo de Cliente: " + v.getCliente().getTipo());
                System.out.println("Asiento: " + v.getAsiento().getNumero());
                System.out.println("Ubicación: " + v.getAsiento().getUbicacion());
                System.out.println("Precio base: $" + v.getAsiento().getPrecio());
                System.out.println("Precio pagado: $" + (int) v.getPrecioPagado());
                System.out.println("\n---------------------------------");
                System.out.println("Gracias por venir a Teatro Moro");
                System.out.println("\n---------------------------------");
                return;
            }
        }
        System.out.println("Venta no encontrada.");
    }

    public static void main(String[] args) {
        TeatroMoro teatro = new TeatroMoro();
        teatro.mostrarMenu();
    }
}
