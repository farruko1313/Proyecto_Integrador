package TercerSprint.control;

import TercerSprint.modelo.Cliente;
import TercerSprint.modelo.Empleado;
import TercerSprint.modelo.Traje;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de la gestión de clientes y sus trajes.
 * Según el enunciado, solo el Maestro puede crear, modificar o borrar clientes.
 *
 * Sprint #3 - [PR4] Clase del Control encargada del manejo de la aplicación.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class ClientesControlador {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final TrajeDAO trajeDAO = new TrajeDAO();

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listarTodos();
    }

    public List<Traje> listarTrajesDe(int idCliente) throws SQLException {
        return trajeDAO.listarPorCliente(idCliente);
    }

    public int crearCliente(Cliente cliente) throws SQLException {
        exigirMaestro();
        return clienteDAO.crear(cliente);
    }

    public boolean modificarCliente(Cliente cliente) throws SQLException {
        exigirMaestro();
        return clienteDAO.modificar(cliente);
    }

    public boolean borrarCliente(int idCliente) throws SQLException {
        exigirMaestro();
        return clienteDAO.borrar(idCliente);
    }

    public int crearTraje(Traje traje) throws SQLException {
        exigirMaestro();
        return trajeDAO.crear(traje);
    }

    public boolean cambiarEstadoTraje(int idTraje, Traje.Estado estado) throws SQLException {
        // Oficial y Maestro pueden cambiar el estado del trabajo
        Empleado emp = SesionActual.getEmpleado();
        if (emp == null || !emp.puedeCrearCitas()) {
            throw new IllegalStateException("Solo Oficiales o Maestros pueden cambiar el estado del traje.");
        }
        return trajeDAO.cambiarEstado(idTraje, estado);
    }

    public boolean borrarTraje(int idTraje) throws SQLException {
        exigirMaestro();
        return trajeDAO.borrar(idTraje);
    }

    // --------------------------------------------------- helpers
    private void exigirMaestro() {
        Empleado emp = SesionActual.getEmpleado();
        if (emp == null || !emp.esMaestro()) {
            throw new IllegalStateException("Operación reservada al Maestro.");
        }
    }
}
