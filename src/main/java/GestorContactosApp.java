import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GestorContactosApp {

    private static final String URL = "jdbc:mysql://localhost:3306/gestor_contactos";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n==============================");
            System.out.println("    GESTOR DE CONTACTOS       ");
            System.out.println("==============================");
            System.out.println("1. Listar contactos");
            System.out.println("2. Añadir contacto");
            System.out.println("3. Editar contacto");
            System.out.println("4. Eliminar contacto");
            System.out.println("5. Salir");
            System.out.print("Elige una opción (1-5): ");

            String entrada = scanner.nextLine().trim();

            switch (entrada) {
                case "1":
                    listarContactos(scanner);
                    break;
                case "2":
                    addContacto(scanner);
                    break;
                case "3":
                    editarContacto(scanner);
                    break;
                case "4":
                    eliminarContacto(scanner);
                    break;
                case "5":
                    System.out.println("\n¡Hasta luego!");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, introduce un número del 1 al 5.");
            }
        }

        scanner.close();
    }

    private static void listarContactos(Scanner scanner) {
        String sql = "SELECT * FROM contactos";

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- LISTA DE CONTACTOS ---");
            boolean hayContactos = false;

            while (rs.next()) {
                hayContactos = true;
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");

                System.out.println("ID: " + id + " | Nombre: " + nombre + " | Teléfono: " + telefono + " | Email: " + email);
            }

            if (!hayContactos) {
                System.out.println("La base de datos está vacía.");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar los contactos: " + e.getMessage());
        }

        pausa(scanner);
    }

    private static void addContacto(Scanner scanner) {
        System.out.println("\n--- AÑADIR CONTACTO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO contactos (nombre, telefono, email) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, telefono);
            pstmt.setString(3, email);

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                System.out.println("¡Contacto guardado con éxito!");
            }

        } catch (SQLException e) {
            System.err.println("Error al añadir contacto: " + e.getMessage());
        }

        pausa(scanner);
    }

    private static void editarContacto(Scanner scanner) {
        System.out.println("\n--- EDITAR CONTACTO ---");
        System.out.print("Introduce el ID del contacto a editar: ");
        String entradaId = scanner.nextLine().trim();

        try {
            int id = Integer.parseInt(entradaId);

            System.out.print("Nuevo Nombre: ");
            String nuevoNombre = scanner.nextLine();
            System.out.print("Nuevo Teléfono: ");
            String nuevoTelefono = scanner.nextLine();
            System.out.print("Nuevo Email: ");
            String nuevoEmail = scanner.nextLine();

            String sql = "UPDATE contactos SET nombre = ?, telefono = ?, email = ? WHERE id = ?";

            try (Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, nuevoNombre);
                pstmt.setString(2, nuevoTelefono);
                pstmt.setString(3, nuevoEmail);
                pstmt.setInt(4, id);

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    System.out.println("¡Contacto actualizado con éxito!");
                } else {
                    System.out.println("No se encontró ningún contacto con ID " + id + ".");
                }

            } catch (SQLException e) {
                System.err.println("Error al editar contacto: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("El ID ingresado no es un número válido.");
        }

        pausa(scanner);
    }

    private static void eliminarContacto(Scanner scanner) {
        System.out.println("\n--- ELIMINAR CONTACTO ---");
        System.out.print("Introduce el ID del contacto a eliminar: ");
        String entradaId = scanner.nextLine().trim();

        try {
            int id = Integer.parseInt(entradaId);
            String sql = "DELETE FROM contactos WHERE id = ?";

            try (Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    System.out.println("¡Contacto con ID " + id + " eliminado con éxito!");
                } else {
                    System.out.println("No se encontró ningún contacto con ID " + id + ".");
                }

            } catch (SQLException e) {
                System.err.println("Error al eliminar contacto: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("El ID ingresado no es un número válido.");
        }

        pausa(scanner);
    }

    private static void pausa(Scanner scanner) {
        System.out.println("\n--- [ Pulsa ENTER para volver al menú ] ---");
        scanner.nextLine();
    }
}