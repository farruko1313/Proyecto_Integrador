package TercerSprint.control;

import TercerSprint.modelo.Empleado;

/**
 * Mantiene la referencia al empleado autenticado en la aplicación.
 * Singleton sencillo accesible desde cualquier controlador o vista.
 *
 * Sprint #3 - [PR4] Clase del Control encargada del manejo de la aplicación.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public final class SesionActual {

    private static Empleado empleadoLogueado;

    private SesionActual() {}

    public static void iniciar(Empleado e) {
        empleadoLogueado = e;
    }

    public static void cerrar() {
        empleadoLogueado = null;
    }

    public static Empleado getEmpleado() {
        return empleadoLogueado;
    }

    public static boolean haySesion() {
        return empleadoLogueado != null;
    }
}
