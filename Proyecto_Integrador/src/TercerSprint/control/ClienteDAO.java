package TercerSprint.control;

import TercerSprint.modelo.Cliente;
import TercerSprint.modelo.Cliente.Tipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad {@link Cliente}.
 * CRUD completo sobre la tabla <code>clientes</code>.
 *
 * Sprint #3 - [PR4] Clase del Control encargada del acceso a BBDD.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class ClienteDAO {

    private Cliente map(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setNumeroCliente(rs.getInt("id_cliente"));
        c.setNombre(rs.getString("nombre_superheroe"));
        c.setSuperpoder(rs.getString("superpoder"));
        c.setColores(rs.getString("colores"));
        c.setTipo(Tipo.fromTexto(rs.getString("tipo")));
        return c;
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre_superheroe";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    /**
     * Crea un nuevo cliente. Devuelve el id generado.
     */
    public int crear(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes(nombre_superheroe, superpoder, colores, tipo) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getSuperpoder());
            ps.setString(3, cliente.getColores());
            ps.setString(4, cliente.getTipo() != null ? cliente.getTipo().getTexto() : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    cliente.setNumeroCliente(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public boolean modificar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre_superheroe = ?, superpoder = ?, "
                   + "colores = ?, tipo = ? WHERE id_cliente = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getSuperpoder());
            ps.setString(3, cliente.getColores());
            ps.setString(4, cliente.getTipo() != null ? cliente.getTipo().getTexto() : null);
            ps.setInt(5, cliente.getNumeroCliente());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean borrar(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        }
    }
}
