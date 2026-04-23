package SegundoSprint.vista;

import TercerSprint.control.CitasControlador;
import TercerSprint.control.ClientesControlador;
import TercerSprint.control.SesionActual;
import TercerSprint.control.TalleresControlador;
import TercerSprint.modelo.Cita;
import TercerSprint.modelo.Cliente;
import TercerSprint.modelo.Taller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Ventana principal de la aplicación del Taller de Edna Moda.
 * Cada item del menú abre una nueva ventana (JFrame) con la información
 * correspondiente y, cuando procede, un diálogo para crear/editar/borrar.
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

    // Item sesión
    private JMenuItem itemCerrarSesion;

    private JLabel lblBienvenida;
    private JPanel panelCentral;

    // Controladores (se crean perezosamente)
    private CitasControlador citasControlador;
    private TalleresControlador talleresControlador;
    private ClientesControlador clientesControlador;

    public VentanaPrincipal(String apodo, String categoria) {
        this.empleadoApodo = apodo;
        this.categoriaEmpleado = categoria;

        setTitle("Taller Edna Moda");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construirMenu();
        construirPanelCentral();
        aplicarPermisos();
        asignarListeners();

        setVisible(true);
    }

    private void construirMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(30, 30, 30));
        menuBar.setOpaque(true);

        menuCitas = crearMenu("Citas");
        itemVerTodasCitas = crearItem("Ver todas las citas");
        itemMisCitas      = crearItem("Mis citas");
        itemNuevaCita     = crearItem("Nueva cita");
        menuCitas.add(itemVerTodasCitas);
        menuCitas.add(itemMisCitas);
        menuCitas.addSeparator();
        menuCitas.add(itemNuevaCita);

        menuTalleres = crearMenu("Talleres");
        itemVerTalleres   = crearItem("Ver talleres");
        itemNuevoTaller   = crearItem("Nuevo taller");
        itemEditarTaller  = crearItem("Editar taller");
        itemBorrarTaller  = crearItem("Borrar taller");
        menuTalleres.add(itemVerTalleres);
        menuTalleres.addSeparator();
        menuTalleres.add(itemNuevoTaller);
        menuTalleres.add(itemEditarTaller);
        menuTalleres.add(itemBorrarTaller);

        menuClientes = crearMenu("Clientes");
        itemVerClientes   = crearItem("Ver clientes");
        itemNuevoCliente  = crearItem("Nuevo cliente");
        itemEditarCliente = crearItem("Editar cliente");
        itemBorrarCliente = crearItem("Borrar cliente");
        menuClientes.add(itemVerClientes);
        menuClientes.addSeparator();
        menuClientes.add(itemNuevoCliente);
        menuClientes.add(itemEditarCliente);
        menuClientes.add(itemBorrarCliente);

        JMenu menuSesion = crearMenu("Sesión");
        itemCerrarSesion = crearItem("Cerrar sesión");
        menuSesion.add(itemCerrarSesion);

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
        menu.setOpaque(true);
        menu.setBackground(new Color(30, 30, 30));
        menu.setFont(new Font("Arial", Font.BOLD, 13));
        return menu;
    }

    private JMenuItem crearItem(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setForeground(Color.BLACK);
        item.setBackground(Color.WHITE);
        item.setOpaque(true);
        item.setFont(new Font("Arial", Font.PLAIN, 13));
        return item;
    }

    private void construirPanelCentral() {
        panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperior.setBackground(new Color(245, 245, 245));
        lblBienvenida = new JLabel("Bienvenido/a, " + empleadoApodo + " (" + categoriaEmpleado + ")");
        lblBienvenida.setFont(new Font("Arial", Font.ITALIC, 13));
        panelSuperior.add(lblBienvenida);

        JPanel panelLogo = new JPanel(new GridBagLayout());
        panelLogo.setBackground(Color.WHITE);
        JLabel lblLogo = new JLabel("<html><div style='text-align:center;'>"
                + "<span style='font-size:36px;'>&#9986;</span><br>"
                + "<b style='font-size:22px;'>TALLER EDNA MODA</b><br>"
                + "<i style='font-size:13px;color:gray;'>No capes.</i>"
                + "</div></html>", SwingConstants.CENTER);
        panelLogo.add(lblLogo);

        panelCentral.add(panelSuperior, BorderLayout.NORTH);
        panelCentral.add(panelLogo, BorderLayout.CENTER);

        add(panelCentral);
    }

    private void aplicarPermisos() {
        switch (categoriaEmpleado) {
            case "Aprendiz":
                itemVerTodasCitas.setEnabled(false);
                itemNuevaCita.setEnabled(false);
                menuTalleres.setEnabled(false);
                menuClientes.setEnabled(false);
                break;
            case "Oficial":
                itemNuevoTaller.setEnabled(false);
                itemEditarTaller.setEnabled(false);
                itemBorrarTaller.setEnabled(false);
                menuClientes.setEnabled(false);
                break;
            case "Maestro":
                break;
            default:
                menuCitas.setEnabled(false);
                menuTalleres.setEnabled(false);
                menuClientes.setEnabled(false);
        }
    }

    // =====================================================================
    // LISTENERS
    // =====================================================================
    private void asignarListeners() {
        itemCerrarSesion.addActionListener(e -> {
            SesionActual.cerrar();
            dispose();
            LoginVista vista = new LoginVista();
            new TercerSprint.control.LoginControlador(vista);
        });

        // Citas
        itemVerTodasCitas.addActionListener(e -> abrirVentanaCitas(false, "Todas las citas"));
        itemMisCitas.addActionListener(e -> abrirVentanaCitas(true, "Mis citas"));
        itemNuevaCita.addActionListener(e -> dialogoNuevaCita());

        // Talleres
        itemVerTalleres.addActionListener(e -> abrirVentanaTalleres("Lista de Talleres"));
        itemNuevoTaller.addActionListener(e -> dialogoNuevoTaller());
        itemEditarTaller.addActionListener(e -> dialogoEditarTaller());
        itemBorrarTaller.addActionListener(e -> dialogoBorrarTaller());

        // Clientes
        itemVerClientes.addActionListener(e -> abrirVentanaClientes("Clientes"));
        itemNuevoCliente.addActionListener(e -> dialogoNuevoCliente());
        itemEditarCliente.addActionListener(e -> dialogoEditarCliente());
        itemBorrarCliente.addActionListener(e -> dialogoBorrarCliente());
    }

    // =====================================================================
    // VENTANAS DE LISTADO
    // =====================================================================

    private void abrirVentanaCitas(boolean soloMias, String titulo) {
        if (citasControlador == null) citasControlador = new CitasControlador();

        JFrame ventana = new JFrame("Taller Edna Moda - " + titulo);
        ventana.setSize(900, 500);
        ventana.setLocationRelativeTo(this);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 15, 8, 10));

        String[] columnas = {
            "ID", "Cliente", "Traje", "Taller",
            "Empleado resp.", "Día", "Hora", "Duración (h)"
        };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(tabla);

        JLabel estado = new JLabel(" ");
        estado.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 10));
        estado.setForeground(new Color(100, 100, 100));

        try {
            List<Cita> citas = soloMias
                ? citasControlador.listarMisCitas()
                : citasControlador.listarCitasVisibles();
            for (Cita c : citas) {
                modelo.addRow(new Object[]{
                    c.getIdentificador(),
                    "#" + c.getIdCliente(),
                    "#" + c.getIdTraje(),
                    "#" + c.getIdTaller(),
                    "#" + c.getIdEmpleadoResponsable(),
                    c.getDia(),
                    c.getHora(),
                    c.getDuracion()
                });
            }
            estado.setText("Mostrando " + citas.size() + " cita(s).");
        } catch (SQLException ex) {
            estado.setText("Error al cargar citas: " + ex.getMessage());
            estado.setForeground(new Color(180, 0, 0));
            ex.printStackTrace();
        } catch (IllegalStateException ex) {
            estado.setText(ex.getMessage());
            estado.setForeground(new Color(180, 0, 0));
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(ev -> ventana.dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCerrar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Color.WHITE);
        sur.add(estado, BorderLayout.WEST);
        sur.add(panelBotones, BorderLayout.EAST);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        ventana.setContentPane(panel);
        ventana.setVisible(true);
    }

    private void abrirVentanaTalleres(String titulo) {
        if (talleresControlador == null) talleresControlador = new TalleresControlador();

        JFrame ventana = new JFrame("Taller Edna Moda - " + titulo);
        ventana.setSize(650, 450);
        ventana.setLocationRelativeTo(this);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 15, 8, 10));

        String[] columnas = {"ID", "Nombre de sala", "Tipo de sala"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(tabla);

        JLabel estado = new JLabel(" ");
        estado.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 10));
        estado.setForeground(new Color(100, 100, 100));

        try {
            List<Taller> talleres = talleresControlador.listarTalleres();
            for (Taller t : talleres) {
                modelo.addRow(new Object[]{
                    t.getNumeroTaller(),
                    t.getNombreSala(),
                    t.getTipoSala() != null ? t.getTipoSala().getTexto() : ""
                });
            }
            estado.setText("Mostrando " + talleres.size() + " taller(es).");
        } catch (SQLException ex) {
            estado.setText("Error al cargar talleres: " + ex.getMessage());
            estado.setForeground(new Color(180, 0, 0));
            ex.printStackTrace();
        } catch (IllegalStateException ex) {
            estado.setText(ex.getMessage());
            estado.setForeground(new Color(180, 0, 0));
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(ev -> ventana.dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCerrar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Color.WHITE);
        sur.add(estado, BorderLayout.WEST);
        sur.add(panelBotones, BorderLayout.EAST);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        ventana.setContentPane(panel);
        ventana.setVisible(true);
    }

    private void abrirVentanaClientes(String titulo) {
        if (clientesControlador == null) clientesControlador = new ClientesControlador();

        JFrame ventana = new JFrame("Taller Edna Moda - " + titulo);
        ventana.setSize(800, 450);
        ventana.setLocationRelativeTo(this);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 15, 8, 10));

        String[] columnas = {
            "ID", "Nombre superhéroe/villano", "Superpoder", "Colores", "Tipo"
        };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(tabla);

        JLabel estado = new JLabel(" ");
        estado.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 10));
        estado.setForeground(new Color(100, 100, 100));

        try {
            List<Cliente> clientes = clientesControlador.listarClientes();
            for (Cliente c : clientes) {
                modelo.addRow(new Object[]{
                    c.getNumeroCliente(),
                    c.getNombre(),
                    c.getSuperpoder(),
                    c.getColores(),
                    c.getTipo() != null ? c.getTipo().getTexto() : ""
                });
            }
            estado.setText("Mostrando " + clientes.size() + " cliente(s).");
        } catch (SQLException ex) {
            estado.setText("Error al cargar clientes: " + ex.getMessage());
            estado.setForeground(new Color(180, 0, 0));
            ex.printStackTrace();
        } catch (IllegalStateException ex) {
            estado.setText(ex.getMessage());
            estado.setForeground(new Color(180, 0, 0));
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(ev -> ventana.dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCerrar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Color.WHITE);
        sur.add(estado, BorderLayout.WEST);
        sur.add(panelBotones, BorderLayout.EAST);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        ventana.setContentPane(panel);
        ventana.setVisible(true);
    }

    // =====================================================================
    // DIÁLOGOS DE ALTA / EDICIÓN / BORRADO
    // =====================================================================

    /** Formulario para crear una nueva cita. */
    private void dialogoNuevaCita() {
        if (citasControlador == null) citasControlador = new CitasControlador();

        JTextField txtCliente   = new JTextField();
        JTextField txtTraje     = new JTextField();
        JTextField txtTaller    = new JTextField();
        JTextField txtEmpResp   = new JTextField();
        JTextField txtDia       = new JTextField(LocalDate.now().toString());
        JTextField txtHora      = new JTextField("10:00");
        JTextField txtDuracion  = new JTextField("1");

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("ID cliente:"));           form.add(txtCliente);
        form.add(new JLabel("ID traje:"));             form.add(txtTraje);
        form.add(new JLabel("ID taller:"));            form.add(txtTaller);
        form.add(new JLabel("ID empleado responsable:")); form.add(txtEmpResp);
        form.add(new JLabel("Día (YYYY-MM-DD):"));     form.add(txtDia);
        form.add(new JLabel("Hora (HH:MM):"));         form.add(txtHora);
        form.add(new JLabel("Duración (horas):"));     form.add(txtDuracion);

        int res = JOptionPane.showConfirmDialog(this, form,
            "Nueva cita", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            int idCliente  = Integer.parseInt(txtCliente.getText().trim());
            int idTraje    = Integer.parseInt(txtTraje.getText().trim());
            int idTaller   = Integer.parseInt(txtTaller.getText().trim());
            int idEmpResp  = Integer.parseInt(txtEmpResp.getText().trim());
            LocalDate dia  = LocalDate.parse(txtDia.getText().trim());
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            int duracion   = Integer.parseInt(txtDuracion.getText().trim());

            Cita cita = CitasControlador.construir(idCliente, idTraje, idEmpResp,
                idTaller, dia, hora, duracion);
            int id = citasControlador.crearCita(cita);
            JOptionPane.showMessageDialog(this,
                "Cita creada correctamente con ID " + id + ".",
                "Nueva cita", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Los IDs y la duración deben ser números enteros.",
                "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha u hora incorrecto.",
                "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la cita: " + ex.getMessage(),
                "Error BBDD", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "No permitido", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Formulario para crear un nuevo taller. */
    private void dialogoNuevoTaller() {
        if (talleresControlador == null) talleresControlador = new TalleresControlador();

        JTextField txtNombre = new JTextField();
        JComboBox<Taller.TipoSala> cmbTipo = new JComboBox<>(Taller.TipoSala.values());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nombre de sala:"));  form.add(txtNombre);
        form.add(new JLabel("Tipo de sala:"));    form.add(cmbTipo);

        int res = JOptionPane.showConfirmDialog(this, form,
            "Nuevo taller", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Taller t = new Taller(0, txtNombre.getText().trim(),
                (Taller.TipoSala) cmbTipo.getSelectedItem());
            int id = talleresControlador.crearTaller(t);
            JOptionPane.showMessageDialog(this,
                "Taller creado con ID " + id + ".",
                "Nuevo taller", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar el taller: " + ex.getMessage(),
                "Error BBDD", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "No permitido", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Formulario para editar un taller existente por su ID. */
    private void dialogoEditarTaller() {
        if (talleresControlador == null) talleresControlador = new TalleresControlador();

        String sId = JOptionPane.showInputDialog(this,
            "ID del taller a editar:", "Editar taller", JOptionPane.QUESTION_MESSAGE);
        if (sId == null || sId.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(sId.trim());

            JTextField txtNombre = new JTextField();
            JComboBox<Taller.TipoSala> cmbTipo = new JComboBox<>(Taller.TipoSala.values());

            JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
            form.add(new JLabel("Nuevo nombre:"));   form.add(txtNombre);
            form.add(new JLabel("Nuevo tipo:"));     form.add(cmbTipo);

            int res = JOptionPane.showConfirmDialog(this, form,
                "Editar taller #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return;

            Taller t = new Taller(id, txtNombre.getText().trim(),
                (Taller.TipoSala) cmbTipo.getSelectedItem());
            boolean ok = talleresControlador.modificarTaller(t);
            JOptionPane.showMessageDialog(this,
                ok ? "Taller modificado." : "No se ha modificado ninguna fila.",
                "Editar taller",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El ID debe ser un número.", "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(), "Error BBDD", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "No permitido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void dialogoBorrarTaller() {
        if (talleresControlador == null) talleresControlador = new TalleresControlador();

        String sId = JOptionPane.showInputDialog(this,
            "ID del taller a borrar:", "Borrar taller", JOptionPane.QUESTION_MESSAGE);
        if (sId == null || sId.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(sId.trim());
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres borrar el taller #" + id + "?",
                "Confirmar borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            boolean ok = talleresControlador.borrarTaller(id);
            JOptionPane.showMessageDialog(this,
                ok ? "Taller borrado." : "No se ha borrado ninguna fila.",
                "Borrar taller",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El ID debe ser un número.", "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(), "Error BBDD", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "No permitido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void dialogoNuevoCliente() {
        if (clientesControlador == null) clientesControlador = new ClientesControlador();

        JTextField txtNombre     = new JTextField();
        JTextField txtSuperpoder = new JTextField();
        JTextField txtColores    = new JTextField();
        JComboBox<Cliente.Tipo> cmbTipo = new JComboBox<>(Cliente.Tipo.values());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nombre:"));       form.add(txtNombre);
        form.add(new JLabel("Superpoder:"));   form.add(txtSuperpoder);
        form.add(new JLabel("Colores:"));      form.add(txtColores);
        form.add(new JLabel("Tipo:"));         form.add(cmbTipo);

        int res = JOptionPane.showConfirmDialog(this, form,
            "Nuevo cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Cliente c = new Cliente(0,
                txtNombre.getText().trim(),
                txtSuperpoder.getText().trim(),
                txtColores.getText().trim(),
                (Cliente.Tipo) cmbTipo.getSelectedItem());
            int id = clientesControlador.crearCliente(c);
            JOptionPane.showMessageDialog(this,
                "Cliente creado con ID " + id + ".",
                "Nuevo cliente", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar el cliente: " + ex.getMessage(),
                "Error BBDD", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "No permitido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void dialogoEditarCliente() {
        if (clientesControlador == null) clientesControlador = new ClientesControlador();

        String sId = JOptionPane.showInputDialog(this,
            "ID del cliente a editar:", "Editar cliente", JOptionPane.QUESTION_MESSAGE);
        if (sId == null || sId.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(sId.trim());

            JTextField txtNombre     = new JTextField();
            JTextField txtSuperpoder = new JTextField();
            JTextField txtColores    = new JTextField();
            JComboBox<Cliente.Tipo> cmbTipo = new JComboBox<>(Cliente.Tipo.values());

            JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
            form.add(new JLabel("Nombre:"));       form.add(txtNombre);
            form.add(new JLabel("Superpoder:"));   form.add(txtSuperpoder);
            form.add(new JLabel("Colores:"));      form.add(txtColores);
            form.add(new JLabel("Tipo:"));         form.add(cmbTipo);

            int res = JOptionPane.showConfirmDialog(this, form,
                "Editar cliente #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return;

            Cliente c = new Cliente(id,
                txtNombre.getText().trim(),
                txtSuperpoder.getText().trim(),
                txtColores.getText().trim(),
                (Cliente.Tipo) cmbTipo.getSelectedItem());
            boolean ok = clientesControlador.modificarCliente(c);
            JOptionPane.showMessageDialog(this,
                ok ? "Cliente modificado." : "No se ha modificado ninguna fila.",
                "Editar cliente",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El ID debe ser un número.", "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(), "Error BBDD", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "No permitido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void dialogoBorrarCliente() {
        if (clientesControlador == null) clientesControlador = new ClientesControlador();

        String sId = JOptionPane.showInputDialog(this,
            "ID del cliente a borrar:", "Borrar cliente", JOptionPane.QUESTION_MESSAGE);
        if (sId == null || sId.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(sId.trim());
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres borrar el cliente #" + id + "?",
                "Confirmar borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            boolean ok = clientesControlador.borrarCliente(id);
            JOptionPane.showMessageDialog(this,
                ok ? "Cliente borrado." : "No se ha borrado ninguna fila.",
                "Borrar cliente",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El ID debe ser un número.", "Datos no válidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(), "Error BBDD", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "No permitido", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Getters
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

    public void establecerPanelNuevo(JPanel panelNuevo) {
        getContentPane().removeAll();
        panelCentral = panelNuevo;
        add(panelCentral);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new VentanaPrincipal("EdnaM", "Maestro");
    }
}
