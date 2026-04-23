package TercerSprint.control;

import TercerSprint.modelo.Empleado;
import TercerSprint.modelo.Empleado.Categoria;
import TercerSprint.modelo.Taller;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de la gestión de talleres (salas).
 * Reglas:
 * <ul>
 *   <li>Aprendiz: sin acceso.</li>
 *   <li>Oficial: solo puede consultar la lista.</li>
 *   <li>Maestro: añadir, modificar y borrar talleres.</li>
 * </ul>
 *
 * Sprint #3 - [PR4] Clase del Control encargada del manejo de la aplicación.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class TalleresControlador {

    private final TallerDAO tallerDAO = new TallerDAO();

    public List<Taller> listarTalleres() throws SQLException {
        Empleado emp = SesionActual.getEmpleado();
        if (emp == null || emp.getCategoria() == Categoria.APRENDIZ) {
            throw new IllegalStateException("Sin permisos para ver talleres.");
        }
        return tallerDAO.listarTodos();
    }

    public int crearTaller(Taller taller) throws SQLException {
        exigirMaestro();
        return tallerDAO.crear(taller);
    }

    public boolean modificarTaller(Taller taller) throws SQLException {
        exigirMaestro();
        return tallerDAO.modificar(taller);
    }

    public boolean borrarTaller(int idTaller) throws SQLException {
        exigirMaestro();
        return tallerDAO.borrar(idTaller);
    }

    private void exigirMaestro() {
        Empleado emp = SesionActual.getEmpleado();
        if (emp == null || !emp.esMaestro()) {
            throw new IllegalStateException("Operación reservada al Maestro.");
        }
    }
}
