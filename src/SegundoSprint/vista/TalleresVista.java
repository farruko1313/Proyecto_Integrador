package SegundoSprint.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista para la gestión de talleres del Estudio de Edna Moda.
 * Solo los Maestros pueden crear, editar y borrar talleres.
 * Los Oficiales solo pueden ver el listado.
 */
public class TalleresVista extends JFrame {

    private JTable tablaTalleres;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevoTaller;
    private JButton btnEditarTaller;
    private JButton btnBorrarTaller;
    private JButton btnCerrar;

    private static final String[] COLUMNAS = {"ID", "Nombre de sala", "Tipo de sala"};

    /**
     * @param categoria Categoría del empleado: Aprendiz, Oficial o Maestro
     */
    public TalleresVista(String categoria) {
        setTitle("Taller Edna Moda - Talleres");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construirUI();

        // Solo el Maestro puede gestionar talleres
        boolean esMaestro = "Maestro".equals(categoria);
        btnNuevoTaller.setEnabled(esMaestro);
        btnEditarTaller.setEnabled(esMaestro);
        btnBorrarTaller.setEnabled(esMaestro);

        setVisible(true);
    }

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Lista de Talleres", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        // Tabla
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaTalleres = new JTable(modeloTabla);
        tablaTalleres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaTalleres.setRowHeight(22);
        tablaTalleres.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(tablaTalleres);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnNuevoTaller  = new JButton("Nuevo taller");
        btnEditarTaller = new JButton("Editar");
        btnBorrarTaller = new JButton("Borrar");
        btnCerrar       = new JButton("Cerrar");

        estilizarBoton(btnNuevoTaller,  new Color(40, 167, 69),   Color.WHITE);
        estilizarBoton(btnEditarTaller, new Color(0, 123, 255),   Color.WHITE);
        estilizarBoton(btnBorrarTaller, new Color(220, 53, 69),   Color.WHITE);
        estilizarBoton(btnCerrar,       new Color(108, 117, 125), Color.WHITE);

        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnNuevoTaller);
        panelBotones.add(btnEditarTaller);
        panelBotones.add(btnBorrarTaller);
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

    public void cargarTalleres(Object[][] datos) {
        modeloTabla.setRowCount(0);
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    public int getIdTallerSeleccionado() {
        int fila = tablaTalleres.getSelectedRow();
        if (fila == -1) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public JButton getBtnNuevoTaller()  { return btnNuevoTaller; }
    public JButton getBtnEditarTaller() { return btnEditarTaller; }
    public JButton getBtnBorrarTaller() { return btnBorrarTaller; }

    public static void main(String[] args) {
        new TalleresVista("Maestro");
    }
}
