package TercerSprint.control;

import TercerSprint.modelo.Empleado;
import TercerSprint.modelo.Empleado.Categoria;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad {@link Empleado}.
 * Se encarga del acceso a la tabla <code>empleados</code> de la BBDD.
 *
 * Sprint #3 - [PR4] Clase del Control encargada del acceso a BBDD.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class EmpleadoDAO {

    /** Convierte una fila del ResultSet en un objeto Empleado. */
    private Empleado map(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdentificador(rs.getInt("id_empleado"));
        e.setNombreApellidos(rs.getString("nombre") + " " + rs.getString("apellidos"));
        e.setApodo(rs.getString("apodo"));
        e.setCategoria(Categoria.fromTexto(rs.getString("categoria")));
        e.setPasswordHash(rs.getString("password"));
        return e;
    }

    /**
     * Autentica al empleado por apodo + contraseña en claro.
     * La contraseña se cifra en SHA-256 para compararla con la almacenada.
     *
     * @return el {@link Empleado} si las credenciales son válidas, null en caso contrario.
     */
    public Empleado autenticar(String apodo, String passwordEnClaro) throws SQLException {
        String hash = sha256(passwordEnClaro);
        String sql = "SELECT * FROM empleados WHERE apodo = ? AND password = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, apodo);
            ps.setString(2, hash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    /** Devuelve todos los empleados. */
    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados ORDER BY categoria, apodo";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    /** Devuelve los empleados filtrados por categoría. */
    public List<Empleado> listarPorCategoria(Categoria categoria) throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados WHERE categoria = ? ORDER BY apodo";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, categoria.getTexto());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        }
        return lista;
    }

    public Empleado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    // =====================================================================
    // Utilidad: SHA-256 (la misma función que SHA2('x',256) de MySQL)
    // =====================================================================
    public static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException ex) {
            throw new RuntimeException("Error al calcular SHA-256", ex);
        }
    }
}
