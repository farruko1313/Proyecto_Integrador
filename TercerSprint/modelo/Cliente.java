package TercerSprint.modelo;

/**
 * Representa a un cliente del taller: un superhéroe o supervillano.
 *
 * Sprint #3 - [PR4] Clase del Modelo generada a partir del diagrama de clases.
 * Incluye el atributo "tipo" del BONUS (Heroe / Villano) para evitar que
 * dos clientes de tipos opuestos tengan citas consecutivas.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class Cliente {

    /** Tipo de cliente (BONUS - evitar héroes/villanos consecutivos). */
    public enum Tipo {
        HEROE("Heroe"),
        VILLANO("Villano");

        private final String texto;
        Tipo(String t) { this.texto = t; }
        public String getTexto() { return texto; }
        public static Tipo fromTexto(String texto) {
            if (texto == null) return null;
            for (Tipo t : values()) {
                if (t.texto.equalsIgnoreCase(texto)) return t;
            }
            return null;
        }
    }

    private int numeroCliente;          // identificador único
    private String nombre;              // nombre de superhéroe / supervillano
    private String superpoder;
    private String colores;
    private Tipo tipo;                  // Héroe o Villano (puede ser null)

    public Cliente() {}

    public Cliente(int numeroCliente, String nombre, String superpoder,
                   String colores, Tipo tipo) {
        this.numeroCliente = numeroCliente;
        this.nombre = nombre;
        this.superpoder = superpoder;
        this.colores = colores;
        this.tipo = tipo;
    }

    // ---------------------------------------------------------- getters/setters
    public int getNumeroCliente()           { return numeroCliente; }
    public void setNumeroCliente(int n)     { this.numeroCliente = n; }

    public String getNombre()               { return nombre; }
    public void setNombre(String nombre)    { this.nombre = nombre; }

    public String getSuperpoder()           { return superpoder; }
    public void setSuperpoder(String s)     { this.superpoder = s; }

    public String getColores()              { return colores; }
    public void setColores(String c)        { this.colores = c; }

    public Tipo getTipo()                   { return tipo; }
    public void setTipo(Tipo tipo)          { this.tipo = tipo; }

    @Override
    public String toString() {
        return nombre + (tipo != null ? " [" + tipo.getTexto() + "]" : "");
    }
}
