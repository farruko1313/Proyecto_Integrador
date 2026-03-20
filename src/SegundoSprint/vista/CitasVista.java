package SegundoSprint.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Vista para la gestión de citas del Taller de Edna Moda.
 * Muestra un listado de citas con opciones de crear, editar y borrar
 * según el rol del empleado.
 */
public class CitasVista extends JFrame {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevaCita;
    private JButton btnEditarCita;
    private JButton btnBorrarCita;
    private JButton btnCerrar;
    private JComboBox<String> cmbFiltro;
    private String categoria;

    private static final String[] COLUMNAS = {
        "ID", "Cliente", "Traje", "Taller", "Empleado responsable", "Día", "Hora", "Duración (h)"
    };

    /**
     * @param categoria Categoría del empleado: Aprendiz, Oficial o Maestro
     */
    public CitasVista(String categoria) {
        this.categoria = categoria;
        setTitle("Taller Edna Moda - Gestión de Citas");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construirUI();
        aplicarPermisos();

        setVisible(true);
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));

        // Panel superior: título y filtro
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel lblTitulo = new JLabel("Citas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        cmbFiltro = new JComboBox<>(new String[]{"Todas las citas", "Mis citas"});
        panelSuperior.add(lblTitulo);
        panelSuperior.add(new JLabel("Mostrar:"));
        panelSuperior.add(cmbFiltro);

        // Tabla de citas
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCitas.setRowHeight(22);
        tablaCitas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(tablaCitas);

        // Panel inferior: botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnNuevaCita  = new JButton("Nueva cita");
        btnEditarCita = new JButton("Editar");
        btnBorrarCita = new JButton("Borrar");
        btnCerrar     = new JButton("Cerrar");

        estilizarBoton(btnNuevaCita,  new Color(40, 167, 69),  Color.WHITE);
        estilizarBoton(btnEditarCita, new Color(0, 123, 255),  Color.WHITE);
        estilizarBoton(btnBorrarCita, new Color(220, 53, 69),  Color.WHITE);
        estilizarBoton(btnCerrar,     new Color(108, 117, 125), Color.WHITE);

        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnNuevaCita);
        panelBotones.add(btnEditarCita);
        panelBotones.add(btnBorrarCita);
        panelBotones.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll,        BorderLayout.CENTER);
        add(panelBotones,  BorderLayout.SOUTH);
    }

    private void aplicarPermisos() {
        switch (categoria) {
            case "Aprendiz":
                btnNuevaCita.setEnabled(false);
                btnEditarCita.setEnabled(false);
                btnBorrarCita.setEnabled(false);
                cmbFiltro.setSelectedItem("Mis citas");
                cmbFiltro.setEnabled(false);
                break;
            case "Oficial":
                btnBorrarCita.setEnabled(false);
                break;
            case "Maestro":
                // Acceso total
                break;
        }
    }

    private void estilizarBoton(JButton btn, Color fondo, Color texto) {
        btn.setBackground(fondo);
        btn.setForeground(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }

    /** Carga filas en la tabla. Cada Object[] debe tener 8 valores. */
    public void cargarCitas(Object[][] datos) {
        modeloTabla.setRowCount(0);
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    /** Devuelve el ID de la cita seleccionada, o -1 si no hay selección. */
    public int getIdCitaSeleccionada() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public String getFiltroSeleccionado() { return (String) cmbFiltro.getSelectedItem(); }
    public JButton getBtnNuevaCita()      { return btnNuevaCita; }
    public JButton getBtnEditarCita()     { return btnEditarCita; }
    public JButton getBtnBorrarCita()     { return btnBorrarCita; }
    public JComboBox<String> getCmbFiltro() { return cmbFiltro; }

    public static void main(String[] args) {
        new CitasVista("Maestro");
    }
}
