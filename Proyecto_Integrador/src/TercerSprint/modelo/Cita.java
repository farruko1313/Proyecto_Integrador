package TercerSprint.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una cita en el taller de Edna Moda.
 * Una cita reserva un taller para atender a un cliente, trabajando sobre
 * uno de sus trajes, con un empleado responsable (Oficial o Maestro)
 * y, opcionalmente, hasta 2 aprendices.
 *
 * Sprint #3 - [PR4] Clase del Modelo generada a partir del diagrama de clases.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class Cita {

    public static final int MAX_APRENDICES = 2;
    public static final int DURACION_POR_DEFECTO = 1;   // 1 hora

    private int identificador;
    private LocalDate dia;
    private LocalTime hora;
    private int duracion;                  // horas completas, mínimo 1

    // Relaciones (claves foráneas)
    private int idCliente;
    private int idTraje;
    private int idEmpleadoResponsable;     // Oficial o Maestro
    private int idTaller;

    // Lista de aprendices asignados (0..2)
    private List<Integer> idsAprendices = new ArrayList<>();

    public Cita() {}

    public Cita(int identificador, LocalDate dia, LocalTime hora, int duracion,
                int idCliente, int idTraje, int idEmpleadoResponsable, int idTaller) {
        this.identificador = identificador;
        this.dia = dia;
        this.hora = hora;
        this.duracion = duracion <= 0 ? DURACION_POR_DEFECTO : duracion;
        this.idCliente = idCliente;
        this.idTraje = idTraje;
        this.idEmpleadoResponsable = idEmpleadoResponsable;
        this.idTaller = idTaller;
    }

    /**
     * Operación del diagrama: asignarEmpleado().
     * Cambia el empleado responsable de la cita.
     */
    public void asignarEmpleado(int idEmpleado) {
        this.idEmpleadoResponsable = idEmpleado;
    }

    /**
     * Operación del diagrama: cambiarHorario().
     */
    public void cambiarHorario(LocalDate nuevoDia, LocalTime nuevaHora, int nuevaDuracion) {
        this.dia = nuevoDia;
        this.hora = nuevaHora;
        this.duracion = nuevaDuracion <= 0 ? DURACION_POR_DEFECTO : nuevaDuracion;
    }

    /**
     * Asigna un aprendiz adicional a la cita.
     * @return true si se ha podido añadir, false si ya hay {@link #MAX_APRENDICES}.
     */
    public boolean anadirAprendiz(int idAprendiz) {
        if (idsAprendices.size() >= MAX_APRENDICES) return false;
        if (idsAprendices.contains(idAprendiz)) return false;
        return idsAprendices.add(idAprendiz);
    }

    public boolean quitarAprendiz(int idAprendiz) {
        return idsAprendices.remove(Integer.valueOf(idAprendiz));
    }

    /** Hora de fin (hora + duración) — útil para chequeo de solapes. */
    public LocalTime getHoraFin() {
        return hora.plusHours(duracion);
    }

    // ---------------------------------------------------------- getters/setters
    public int getIdentificador()                  { return identificador; }
    public void setIdentificador(int id)           { this.identificador = id; }

    public LocalDate getDia()                      { return dia; }
    public void setDia(LocalDate dia)              { this.dia = dia; }

    public LocalTime getHora()                     { return hora; }
    public void setHora(LocalTime hora)            { this.hora = hora; }

    public int getDuracion()                       { return duracion; }
    public void setDuracion(int duracion)          { this.duracion = duracion; }

    public int getIdCliente()                      { return idCliente; }
    public void setIdCliente(int id)               { this.idCliente = id; }

    public int getIdTraje()                        { return idTraje; }
    public void setIdTraje(int id)                 { this.idTraje = id; }

    public int getIdEmpleadoResponsable()          { return idEmpleadoResponsable; }
    public void setIdEmpleadoResponsable(int id)   { this.idEmpleadoResponsable = id; }

    public int getIdTaller()                       { return idTaller; }
    public void setIdTaller(int id)                { this.idTaller = id; }

    public List<Integer> getIdsAprendices()        { return idsAprendices; }
    public void setIdsAprendices(List<Integer> l)  { this.idsAprendices = (l != null ? l : new ArrayList<>()); }

    @Override
    public String toString() {
        return "Cita #" + identificador + " - " + dia + " " + hora
                + " (" + duracion + "h) cliente#" + idCliente + " taller#" + idTaller;
    }
}
