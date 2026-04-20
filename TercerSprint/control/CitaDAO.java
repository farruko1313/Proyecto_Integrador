package TercerSprint.control;

import TercerSprint.modelo.Cita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad {@link Cita}.
 * CRUD sobre las tablas <code>citas</code> y <code>cita_aprendices</code>.
 *
 * Sprint #3 - [PR4] Clase del Control encargada del acceso a BBDD.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class CitaDAO {

    private Cita map(ResultSet rs) throws SQLException {
        Cita c = new Cita();
        c.setIdentificador(rs.getInt("id_cita"));
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setIdTraje(rs.getInt("id_traje"));
        c.setIdEmpleadoResponsable(rs.getInt("id_empleado_responsable"));
        c.setIdTaller(rs.getInt("id_taller"));
        c.setDia(rs.getDate("dia").toLocalDate());
        c.setHora(rs.getTime("hora").toLocalTime());
        c.setDuracion(rs.getInt("duracion"));
        return c;
    }

    /** Carga la lista de aprendices asignados a la cita dada. */
    private void cargarAprendices(Connection c, Cita cita) throws SQLException {
        String sql = "SELECT id_aprendiz FROM cita_aprendices WHERE id_cita = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cita.getIdentificador());
            try (ResultSet rs = ps.executeQuery()) {
                List<Integer> ids = new ArrayList<>();
                while (rs.next()) ids.add(rs.getInt(1));
                cita.setIdsAprendices(ids);
            }
        }
    }

    /** Lista todas las citas del sistema (con sus aprendices). */
    public List<Cita> listarTodas() throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY dia, hora";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
            for (Cita cita : lista) cargarAprendices(c, cita);
        }
        return lista;
    }

    /** Lista las citas de un empleado (responsable o aprendiz). */
    public List<Cita> listarPorEmpleado(int idEmpleado) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT ci.* FROM citas ci "
                   + "LEFT JOIN cita_aprendices ca ON ca.id_cita = ci.id_cita "
                   + "WHERE ci.id_empleado_responsable = ? OR ca.id_aprendiz = ? "
                   + "ORDER BY ci.dia, ci.hora";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
            for (Cita cita : lista) cargarAprendices(c, cita);
        }
        return lista;
    }

    public Cita buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM citas WHERE id_cita = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cita cita = map(rs);
                    cargarAprendices(c, cita);
                    return cita;
                }
            }
        }
        return null;
    }

    /**
     * Crea una cita y asigna sus aprendices dentro de una transacción.
     * @return id generado.
     */
    public int crear(Cita cita) throws SQLException {
        String insertCita = "INSERT INTO citas(id_cliente, id_traje, id_empleado_responsable, "
                          + "id_taller, dia, hora, duracion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertAprendiz = "INSERT INTO cita_aprendices(id_cita, id_aprendiz) VALUES (?, ?)";

        try (Connection c = ConexionBD.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(insertCita, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, cita.getIdCliente());
                ps.setInt(2, cita.getIdTraje());
                ps.setInt(3, cita.getIdEmpleadoResponsable());
                ps.setInt(4, cita.getIdTaller());
                ps.setDate(5, Date.valueOf(cita.getDia()));
                ps.setTime(6, Time.valueOf(cita.getHora()));
                ps.setInt(7, cita.getDuracion());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) cita.setIdentificador(rs.getInt(1));
                }
            }

            if (cita.getIdsAprendices() != null && !cita.getIdsAprendices().isEmpty()) {
                try (PreparedStatement ps = c.prepareStatement(insertAprendiz)) {
                    for (Integer idA : cita.getIdsAprendices()) {
                        ps.setInt(1, cita.getIdentificador());
                        ps.setInt(2, idA);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            c.commit();
            return cita.getIdentificador();
        }
    }

    /**
     * Modifica los datos principales de la cita y reemplaza su lista de aprendices.
     */
    public boolean modificar(Cita cita) throws SQLException {
        String updateCita = "UPDATE citas SET id_cliente = ?, id_traje = ?, "
                          + "id_empleado_responsable = ?, id_taller = ?, dia = ?, hora = ?, "
                          + "duracion = ? WHERE id_cita = ?";
        String deleteAprendices = "DELETE FROM cita_aprendices WHERE id_cita = ?";
        String insertAprendiz = "INSERT INTO cita_aprendices(id_cita, id_aprendiz) VALUES (?, ?)";

        try (Connection c = ConexionBD.getConnection()) {
            c.setAutoCommit(false);
            int filas;
            try (PreparedStatement ps = c.prepareStatement(updateCita)) {
                ps.setInt(1, cita.getIdCliente());
                ps.setInt(2, cita.getIdTraje());
                ps.setInt(3, cita.getIdEmpleadoResponsable());
                ps.setInt(4, cita.getIdTaller());
                ps.setDate(5, Date.valueOf(cita.getDia()));
                ps.setTime(6, Time.valueOf(cita.getHora()));
                ps.setInt(7, cita.getDuracion());
                ps.setInt(8, cita.getIdentificador());
                filas = ps.executeUpdate();
            }

            try (PreparedStatement ps = c.prepareStatement(deleteAprendices)) {
                ps.setInt(1, cita.getIdentificador());
                ps.executeUpdate();
            }

            if (cita.getIdsAprendices() != null) {
                try (PreparedStatement ps = c.prepareStatement(insertAprendiz)) {
                    for (Integer idA : cita.getIdsAprendices()) {
                        ps.setInt(1, cita.getIdentificador());
                        ps.setInt(2, idA);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            c.commit();
            return filas > 0;
        }
    }

    public boolean borrar(int idCita) throws SQLException {
        String sql = "DELETE FROM citas WHERE id_cita = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Devuelve las citas reservadas en un taller concreto en un día concreto.
     * Útil para detectar solapes de horario y aplicar la regla BONUS
     * (1 h de margen entre héroe y villano).
     */
    public List<Cita> listarPorTallerYDia(int idTaller, java.time.LocalDate dia) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas WHERE id_taller = ? AND dia = ? ORDER BY hora";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idTaller);
            ps.setDate(2, Date.valueOf(dia));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }
        }
        return lista;
    }
}
