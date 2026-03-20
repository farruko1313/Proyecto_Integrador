package SegundoSprint.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana de login para los empleados del taller de Edna Moda.
 * Permite acceder con apodo (nombre en clave) y contraseña.
 */
public class LoginVista extends JFrame {

    private JTextField txtApodo;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;

    public LoginVista() {
        setTitle("Taller de Edna Moda - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Panel título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 30, 30));
        JLabel lblTitulo = new JLabel("TALLER EDNA MODA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelTitulo.add(lblTitulo);

        // Panel formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        JLabel lblApodo = new JLabel("Nombre en clave:");
        txtApodo = new JTextField(15);

        JLabel lblPass = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(30, 30, 30));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));

        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(lblApodo, gbc);
        gbc.gridx = 1;
        panelForm.add(txtApodo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(lblPass, gbc);
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelForm.add(btnLogin, gbc);

        gbc.gridy = 3;
        panelForm.add(lblError, gbc);

        // Acción login (se conectará al controlador)
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String apodo = txtApodo.getText().trim();
                String pass = new String(txtPassword.getPassword());
                if (apodo.isEmpty() || pass.isEmpty()) {
                    lblError.setText("Por favor, rellena todos los campos.");
                } else {
                    // TODO: conectar con Controlador
                    lblError.setText("Credenciales no válidas.");
                }
            }
        });

        // Enter en password también hace login
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        add(panelPrincipal);

        setVisible(true);
    }

    public String getApodo() {
        return txtApodo.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void mostrarError(String mensaje) {
        lblError.setText(mensaje);
    }

    public void limpiarError() {
        lblError.setText(" ");
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public static void main(String[] args) {
        new LoginVista();
    }
}
