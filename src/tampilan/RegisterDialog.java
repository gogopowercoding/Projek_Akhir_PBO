package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.User;
import model.UserDAO;

public class RegisterDialog extends JDialog {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtNamaLengkap;
    private JTextArea txtAlamat;
    private JTextField txtNoTelp;
    private JTextField txtEmail;
    private JButton btnRegister;
    private JButton btnCancel;
    
    public RegisterDialog(JFrame parent) {
        super(parent, "Register - Pembayar Pajak", true);
        
        initComponents();
        setupLayout();
        setupEvents();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);
        txtNamaLengkap = new JTextField(20);
        txtAlamat = new JTextArea(3, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtNoTelp = new JTextField(20);
        txtEmail = new JTextField(20);
        
        btnRegister = new JButton("Register");
        btnCancel = new JButton("Cancel");
        
        btnRegister.setPreferredSize(new Dimension(100, 30));
        btnCancel.setPreferredSize(new Dimension(100, 30));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("REGISTER PEMBAYAR PAJAK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Username *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Password *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Konfirmasi Password *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtConfirmPassword, gbc);
        
        // Nama Lengkap
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Nama Lengkap *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNamaLengkap, gbc);
        
        // Alamat
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(new JScrollPane(txtAlamat), gbc);
        
        // No Telp
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("No. Telepon:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNoTelp, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        // Note
        gbc.gridy = 8;
        JLabel noteLabel = new JLabel("* Field wajib diisi");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        noteLabel.setForeground(Color.GRAY);
        mainPanel.add(noteLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void register() {
        // Validation
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String namaLengkap = txtNamaLengkap.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String noTelp = txtNoTelp.getText().trim();
        String email = txtEmail.getText().trim();
        
        if (username.isEmpty() || password.isEmpty() || namaLengkap.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username, password, dan nama lengkap harus diisi!", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, 
                "Username minimal 3 karakter!", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password minimal 6 karakter!", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Password dan konfirmasi password tidak sama!", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if username already exists
        if (UserDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, 
                "Username sudah terdaftar, silakan gunakan username lain!", 
                "Registration Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNamaLengkap(namaLengkap);
        user.setAlamat(alamat);
        user.setNoTelp(noTelp);
        user.setEmail(email);
        user.setRole("pembayar");
        
        // Register user
        if (UserDAO.register(user)) {
            JOptionPane.showMessageDialog(this, 
                "Registrasi berhasil! Silakan login dengan akun baru Anda.", 
                "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Registrasi gagal! Silakan coba lagi.", 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}