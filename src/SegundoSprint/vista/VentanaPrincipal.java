package SegundoSprint.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana principal de la aplicación del Taller de Edna Moda.
 * Muestra el menú de navegación según el rol del empleado:
 * - Aprendiz: solo ver sus citas
 * - Oficial: ver todas, crear y modificar sus citas, asignar aprendices
 * - Maestro: acceso total
 */
public class VentanaPrincipal extends JFrame {

    private String empleadoApodo;
    private String categoriaEmpleado;

    // Menús
    private JMenu menuCitas;
    private JMenu menuTalleres;
    private JMenu menuClientes;

    // Items Citas
    private JMenuItem itemVerTodasCitas;
    private JMenuItem itemMisCitas;
    private JMenuItem itemNuevaCita;

    // Items Talleres
    private JMenuItem itemVerTalleres;
    private JMenuItem itemNuevoTaller;
    private JMenuItem itemEditarTaller;
    private JMenuItem itemBorrarTaller;

    // Items Clientes
    private JMenuItem itemVerClientes;
    private JMenuItem itemNuevoCliente;
    private JMenuItem itemEditarCliente;
    private JMenuItem itemBorrarCliente;

    // Item sesion
    private JMenuItem itemCerrarSesion;

    private JLabel lblBienvenida;

    /**
     * Constructor de la ventana principal.
     * @param apodo    Apodo del empleado logueado
     * @param categoria Categoría: Aprendiz, Oficial o Maestro
     */
    public VentanaPrincipal(String apodo, String categoria) {
        this.empleadoApodo = apodo;
        this.categoriaEmpleado = categoria;

        setTitle("Taller Edna Moda");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construirMenu();
        construirPanelCentral();
        aplicarPermisos();

        setVisible(true);
    }

    private void construirMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(30, 30, 30));

        // Menú Citas
        menuCitas = crearMenu("Citas");
        itemVerTodasCitas = new JMenuItem("Ver todas las citas");
        itemMisCitas = new JMenuItem("Mis citas");
        itemNuevaCita = new JMenuItem("Nueva cita");
        menuCitas.add(itemVerTodasCitas);
        menuCitas.add(itemMisCitas);
        menuCitas.addSeparator();
        menuCitas.add(itemNuevaCita);

        // Menú Talleres
        menuTalleres = crearMenu("Talleres");
        itemVerTalleres = new JMenuItem("Ver talleres");
        itemNuevoTaller = new JMenuItem("Nuevo taller");
        itemEditarTaller = new JMenuItem("Editar taller");
        itemBorrarTaller = new JMenuItem("Borrar taller");
        menuTalleres.add(itemVerTalleres);
        menuTalleres.addSeparator();
        menuTalleres.add(itemNuevoTaller);
        menuTalleres.add(itemEditarTaller);
        menuTalleres.add(itemBorrarTaller);

        // Menú Clientes
        menuClientes = crearMenu("Clientes");
        itemVerClientes = new JMenuItem("Ver clientes");
        itemNuevoCliente = new JMenuItem("Nuevo cliente");
        itemEditarCliente = new JMenuItem("Editar cliente");
        itemBorrarCliente = new JMenuItem("Borrar cliente");
        menuClientes.add(itemVerClientes);
        menuClientes.addSeparator();
        menuClientes.add(itemNuevoCliente);
        menuClientes.add(itemEditarCliente);
        menuClientes.add(itemBorrarCliente);

        // Menú Sesión
        JMenu menuSesion = crearMenu("Sesión");
        itemCerrarSesion = new JMenuItem("Cerrar sesión");
        menuSesion.add(itemCerrarSesion);

        itemCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginVista();
        });

        menuBar.add(menuCitas);
        menuBar.add(menuTalleres);
        menuBar.add(menuClientes);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuSesion);

        setJMenuBar(menuBar);
    }

    private JMenu crearMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.BOLD, 13));
        return menu;
    }

    private void construirPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);

        // Panel superior con bienvenida
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperior.setBackground(new Color(245, 245, 245));
        lblBienvenida = new JLabel("Bienvenido/a, " + empleadoApodo + " (" + categoriaEmpleado + ")");
        lblBienvenida.setFont(new Font("Arial", Font.ITALIC, 13));
        panelSuperior.add(lblBienvenida);

        // Panel logo central
        JPanel panelLogo = new JPanel(new GridBagLayout());
        panelLogo.setBackground(Color.WHITE);
        JLabel lblLogo = new JLabel("<html><div style='text-align:center;'>"
                + "<span style='font-size:36px;'>✂</span><br>"
                + "<b style='font-size:22px;'>TALLER EDNA MODA</b><br>"
                + "<i style='font-size:13px;color:gray;'>No capes.</i>"
                + "</div></html>", SwingConstants.CENTER);
        panelLogo.add(lblLogo);

        panelCentral.add(panelSuperior, BorderLayout.NORTH);
        panelCentral.add(panelLogo, BorderLayout.CENTER);

        add(panelCentral);
    }

    /**
     * Aplica los permisos de los menús según la categoría del empleado.
     */
    private void aplicarPermisos() {
        switch (categoriaEmpleado) {
            case "Aprendiz":
                // Solo puede ver sus citas
                itemVerTodasCitas.setEnabled(false);
                itemNuevaCita.setEnabled(false);
                menuTalleres.setEnabled(false);
                menuClientes.setEnabled(false);
                break;

            case "Oficial":
                // Puede ver todas las citas, crear las suyas, gestionar talleres (solo ver)
                itemNuevoTaller.setEnabled(false);
                itemEditarTaller.setEnabled(false);
                itemBorrarTaller.setEnabled(false);
                menuClientes.setEnabled(false);
                break;

            case "Maestro":
                // Acceso total, sin restricciones
                break;

            default:
                // Sin permisos por defecto
                menuCitas.setEnabled(false);
                menuTalleres.setEnabled(false);
                menuClientes.setEnabled(false);
        }
    }

    // Getters para que el Controlador pueda asignar listeners
    public JMenuItem getItemVerTodasCitas()   { return itemVerTodasCitas; }
    public JMenuItem getItemMisCitas()         { return itemMisCitas; }
    public JMenuItem getItemNuevaCita()        { return itemNuevaCita; }
    public JMenuItem getItemVerTalleres()      { return itemVerTalleres; }
    public JMenuItem getItemNuevoTaller()      { return itemNuevoTaller; }
    public JMenuItem getItemEditarTaller()     { return itemEditarTaller; }
    public JMenuItem getItemBorrarTaller()     { return itemBorrarTaller; }
    public JMenuItem getItemVerClientes()      { return itemVerClientes; }
    public JMenuItem getItemNuevoCliente()     { return itemNuevoCliente; }
    public JMenuItem getItemEditarCliente()    { return itemEditarCliente; }
    public JMenuItem getItemBorrarCliente()    { return itemBorrarCliente; }
    public JMenuItem getItemCerrarSesion()     { return itemCerrarSesion; }

    public static void main(String[] args) {
        new VentanaPrincipal("EdnaM", "Maestro");
    }
}
