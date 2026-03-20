package SegundoSprint.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista para la gestión de clientes del Taller de Edna Moda.
 * Solo los Maestros pueden añadir, modificar y borrar clientes.
 */
public class ClientesVista extends JFrame {

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevoCliente;
    private JButton btnEditarCliente;
    private JButton btnBorrarCliente;
    private JButton btnVerTrajes;
    private JButton btnCerrar;

    private static final String[] COLUMNAS = {
        "ID", "Nombre superhéroe/villano", "Superpoder", "Colores", "Tipo"
    };

    /**
     * @param categoria Categoría del empleado: Aprendiz, Oficial o Maestro
     */
    public ClientesVista(String categoria) {
        setTitle("Taller Edna Moda - Clientes");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construirUI();

        boolean esMaestro = "Maestro".equals(categoria);
        btnNuevoCliente.setEnabled(esMaestro);
        btnEditarCliente.setEnabled(esMaestro);
        btnBorrarCliente.setEnabled(esMaestro);

        setVisible(true);
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Clientes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        // Tabla
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.setRowHeight(22);
        tablaClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(tablaClientes);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnNuevoCliente  = new JButton("Nuevo cliente");
        btnEditarCliente = new JButton("Editar");
        btnBorrarCliente = new JButton("Borrar");
        btnVerTrajes     = new JButton("Ver trajes");
        btnCerrar        = new JButton("Cerrar");

        estilizarBoton(btnNuevoCliente,  new Color(40, 167, 69),   Color.WHITE);
        estilizarBoton(btnEditarCliente, new Color(0, 123, 255),   Color.WHITE);
        estilizarBoton(btnBorrarCliente, new Color(220, 53, 69),   Color.WHITE);
        estilizarBoton(btnVerTrajes,     new Color(255, 153, 0),   Color.WHITE);
        estilizarBoton(btnCerrar,        new Color(108, 117, 125), Color.WHITE);

        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnNuevoCliente);
        panelBotones.add(btnEditarCliente);
        panelBotones.add(btnBorrarCliente);
        panelBotones.add(btnVerTrajes);
        panelBotones.add(btnCerrar);

        add(lblTitulo,    BorderLayout.NORTH);
        add(scroll,       BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void estilizarBoton(JButton btn, Color fondo, Color texto) {
        btn.setBackground(fondo);
        btn.setForeground(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }

    public void cargarClientes(Object[][] datos) {
        modeloTabla.setRowCount(0);
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    public int getIdClienteSeleccionado() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public JButton getBtnNuevoCliente()  { return btnNuevoCliente; }
    public JButton getBtnEditarCliente() { return btnEditarCliente; }
    public JButton getBtnBorrarCliente() { return btnBorrarCliente; }
    public JButton getBtnVerTrajes()     { return btnVerTrajes; }

    public static void main(String[] args) {
        new ClientesVista("Maestro");
    }
}
