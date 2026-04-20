package TercerSprint.modelo;

/**
 * Representa un traje perteneciente a un cliente.
 * Un cliente puede tener varios trajes en distintos estados.
 *
 * Sprint #3 - [PR4] Clase del Modelo generada a partir del diagrama de clases.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class Traje {

    /** Estado posible del traje según el diagrama (Diseño / Costura / Taller). */
    public enum Estado {
        DISENIO("Diseño"),
        COSTURA("Costura"),
        TALLER("Taller");

        private final String texto;
        Estado(String t) { this.texto = t; }
        public String getTexto() { return texto; }

        public static Estado fromTexto(String texto) {
            if (texto == null) return null;
            for (Estado e : values()) {
                if (e.texto.equalsIgnoreCase(texto)) return e;
            }
            throw new IllegalArgumentException("Estado desconocido: " + texto);
        }
    }

    private int identificador;
    private String nombre;          // "Traje principal", etc.
    private Estado estado;
    private int idCliente;          // FK al cliente propietario

    public Traje() {}

    public Traje(int identificador, int idCliente, String nombre, Estado estado) {
        this.identificador = identificador;
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.estado = estado;
    }

    /**
     * Cambia el estado del traje (operación del diagrama: cambiarEstado()).
     */
    public void cambiarEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // ---------------------------------------------------------- getters/setters
    public int getIdentificador()           { return identificador; }
    public void setIdentificador(int id)    { this.identificador = id; }

    public int getIdCliente()               { return idCliente; }
    public void setIdCliente(int id)        { this.idCliente = id; }

    public String getNombre()               { return nombre; }
    public void setNombre(String nombre)    { this.nombre = nombre; }

    public Estado getEstado()               { return estado; }
    public void setEstado(Estado estado)    { this.estado = estado; }

    @Override
    public String toString() {
        return nombre + " (" + estado.getTexto() + ")";
    }
}
