package TercerSprint.modelo;

/**
 * Representa a un empleado del taller de Edna Moda.
 * Categorías posibles: Aprendiz, Oficial, Maestro.
 *
 * Sprint #3 - [PR4] Clase del Modelo generada a partir del diagrama de clases.
 *
 * @author Equipo Proyecto Integrador 1ºDAW
 */
public class Empleado {

    /** Categorías permitidas para un empleado del taller. */
    public enum Categoria {
        APRENDIZ("Aprendiz"),
        OFICIAL("Oficial"),
        MAESTRO("Maestro");

        private final String texto;

        Categoria(String texto) {
            this.texto = texto;
        }

        public String getTexto() {
            return texto;
        }

        /**
         * Convierte la cadena almacenada en BBDD ("Aprendiz", "Oficial", "Maestro")
         * en el valor del enum correspondiente.
         */
        public static Categoria fromTexto(String texto) {
            if (texto == null) return null;
            for (Categoria c : values()) {
                if (c.texto.equalsIgnoreCase(texto)) return c;
            }
            throw new IllegalArgumentException("Categoría desconocida: " + texto);
        }
    }

    private int identificador;
    private String nombreApellidos;
    private String apodo;          // nombre en clave / usuario de login
    private Categoria categoria;
    private String passwordHash;   // hash SHA-256 (no se expone fuera del modelo)

    /** Constructor vacío necesario para los DAOs. */
    public Empleado() {}

    public Empleado(int identificador, String nombreApellidos, String apodo,
                    Categoria categoria) {
        this.identificador = identificador;
        this.nombreApellidos = nombreApellidos;
        this.apodo = apodo;
        this.categoria = categoria;
    }

    public Empleado(int identificador, String nombreApellidos, String apodo,
                    Categoria categoria, String passwordHash) {
        this(identificador, nombreApellidos, apodo, categoria);
        this.passwordHash = passwordHash;
    }

    // ----------------------------------------------------------------- métodos
    /**
     * Comprueba si el empleado tiene permisos para crear citas.
     * Los aprendices NO pueden crear citas nuevas.
     */
    public boolean puedeCrearCitas() {
        return categoria == Categoria.OFICIAL || categoria == Categoria.MAESTRO;
    }

    /**
     * Comprueba si el empleado tiene permisos totales (Maestro).
     */
    public boolean esMaestro() {
        return categoria == Categoria.MAESTRO;
    }

    // ---------------------------------------------------------- getters/setters
    public int getIdentificador()           { return identificador; }
    public void setIdentificador(int id)    { this.identificador = id; }

    public String getNombreApellidos()                 { return nombreApellidos; }
    public void setNombreApellidos(String n)           { this.nombreApellidos = n; }

    public String getApodo()                { return apodo; }
    public void setApodo(String apodo)      { this.apodo = apodo; }

    public Categoria getCategoria()         { return categoria; }
    public void setCategoria(Categoria c)   { this.categoria = c; }

    public String getPasswordHash()         { return passwordHash; }
    public void setPasswordHash(String p)   { this.passwordHash = p; }

    @Override
    public String toString() {
        return apodo + " (" + categoria.getTexto() + ") - " + nombreApellidos;
    }
}
