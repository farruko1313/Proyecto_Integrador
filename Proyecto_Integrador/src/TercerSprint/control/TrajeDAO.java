package TercerSprint.control;

import TercerSprint.modelo.Traje;
import TercerSprint.modelo.Traje.Estado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad {@link Traje}.
 * CRUD sobre la tabla <code>trajes</code>.
 *
 * Sprint #3 - [PR4] Clase del Control encargada del acceso a BBDD.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class TrajeDAO {

    private Traje map(ResultSet rs) throws SQLException {
        Traje t = new Traje();
        t.setIdentificador(rs.getInt("id_traje"));
        t.setIdCliente(rs.getInt("id_cliente"));
        t.setNombre(rs.getString("nombre"));
        t.setEstado(Estado.fromTexto(rs.getString("estado")));
        return t;
    }

    public List<Traje> listarTodos() throws SQLException {
        List<Traje> lista = new ArrayList<>();
        String sql = "SELECT * FROM trajes ORDER BY id_cliente, nombre";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    /** Devuelve todos los trajes del cliente indicado. */
    public List<Traje> listarPorCliente(int idCliente) throws SQLException {
        List<Traje> lista = new ArrayList<>();
        String sql = "SELECT * FROM trajes WHERE id_cliente = ? ORDER BY nombre";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        }
        return lista;
    }

    public Traje buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM trajes WHERE id_traje = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public int crear(Traje traje) throws SQLException {
        String sql = "INSERT INTO trajes(id_cliente, nombre, estado) VALUES (?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, traje.getIdCliente());
            ps.setString(2, traje.getNombre());
            ps.setString(3, traje.getEstado().getTexto());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    traje.setIdentificador(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public boolean cambiarEstado(int idTraje, Estado nuevoEstado) throws SQLException {
        String sql = "UPDATE trajes SET estado = ? WHERE id_traje = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.getTexto());
            ps.setInt(2, idTraje);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean borrar(int idTraje) throws SQLException {
        String sql = "DELETE FROM trajes WHERE id_traje = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idTraje);
            return ps.executeUpdate() > 0;
        }
    }
}
