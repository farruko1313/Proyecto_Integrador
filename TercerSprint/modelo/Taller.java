package TercerSprint.modelo;

/**
 * Representa un taller (sala) del estudio de Edna Moda.
 * Se identifica por un número único, un nombre de sala (ciudad)
 * y un tipo (Diseño, Costura o Pruebas).
 *
 * Sprint #3 - [PR4] Clase del Modelo generada a partir del diagrama de clases.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class Taller {

    /** Tipos de sala permitidos. */
    public enum TipoSala {
        DISENIO("Diseño"),
        COSTURA("Costura"),
        PRUEBAS("Pruebas");

        private final String texto;
        TipoSala(String t) { this.texto = t; }
        public String getTexto() { return texto; }
        public static TipoSala fromTexto(String texto) {
            if (texto == null) return null;
            for (TipoSala t : values()) {
                if (t.texto.equalsIgnoreCase(texto)) return t;
            }
            throw new IllegalArgumentException("Tipo de sala desconocido: " + texto);
        }
    }

    private int numeroTaller;
    private String nombreSala;    // París, Milán, Madrid, etc.
    private TipoSala tipoSala;

    public Taller() {}

    public Taller(int numeroTaller, String nombreSala, TipoSala tipoSala) {
        this.numeroTaller = numeroTaller;
        this.nombreSala = nombreSala;
        this.tipoSala = tipoSala;
    }

    /**
     * Operación del diagrama: modificarTaller().
     * Actualiza los datos del taller en memoria.
     */
    public void modificarTaller(String nuevoNombre, TipoSala nuevoTipo) {
        this.nombreSala = nuevoNombre;
        this.tipoSala = nuevoTipo;
    }

    // ---------------------------------------------------------- getters/setters
    public int getNumeroTaller()               { return numeroTaller; }
    public void setNumeroTaller(int n)         { this.numeroTaller = n; }

    public String getNombreSala()              { return nombreSala; }
    public void setNombreSala(String n)        { this.nombreSala = n; }

    public TipoSala getTipoSala()              { return tipoSala; }
    public void setTipoSala(TipoSala t)        { this.tipoSala = t; }

    @Override
    public String toString() {
        return tipoSala.getTexto() + " - " + nombreSala;
    }
}
