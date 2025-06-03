package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.User;
import model.UserDAO;
import basisdata.KoneksiBasisData;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    
    public LoginFrame() {
        // Initialize database schema
        KoneksiBasisData.createTablesIfNotExists();
        
        initComponents();
        setupLayout();
        setupEvents();
        
        setTitle("Login - Sistem Perpajakan Kendaraan Bermotor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        
        // Style components
        btnLogin.setPreferredSize(new Dimension(100, 30));
        btnRegister.setPreferredSize(new Dimension(100, 30));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("SISTEM PERPAJAKAN KENDARAAN BERMOTOR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtPassword, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        JLabel infoLabel = new JLabel("<html><center>Default Admin:<br>Username: admin<br>Password: admin123</center></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);
        
        gbc.gridy = 3;
        mainPanel.add(infoPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterDialog();
            }
        });
        
        // Enter key on password field
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username dan password harus diisi!", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        User user = UserDAO.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, 
                "Login berhasil! Selamat datang " + user.getNamaLengkap(),
                "Login Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Open appropriate main frame based on role
            if ("admin".equals(user.getRole())) {
                new AdminMainFrame(user).setVisible(true);
            } else {
                new PembayarMainFrame(user).setVisible(true);
            }
            
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Username atau password salah!", 
                "Login Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
    
    private void openRegisterDialog() {
        RegisterDialog dialog = new RegisterDialog(this);
        dialog.setVisible(true);
    }
}