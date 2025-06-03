package tampilan;

import javax.swing.*;
import java.awt.*;
import model.User;
import model.UserDAO;

class RegisterFrame extends JFrame {
    private JTextField usernameField, namaLengkapField, alamatField, noTelpField, emailField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        setTitle("Register - Sistem Pajak Kendaraan");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Tambahan
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("REGISTRASI PENGGUNA", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields
        String[] labels = {"Username:", "Password:", "Nama Lengkap:", "Alamat:", "No. Telp:", "Email:"};
        JTextField[] fields = new JTextField[6];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            if (i == 1) {
                passwordField = new JPasswordField(20);
                formPanel.add(passwordField, gbc);
            } else {
                fields[i] = new JTextField(20);
                formPanel.add(fields[i], gbc);
            }
        }

        usernameField = fields[0];
        namaLengkapField = fields[2];
        alamatField = fields[3];
        noTelpField = fields[4];
        emailField = fields[5];

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        registerButton.addActionListener(e -> handleRegister());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleRegister() {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword());
    String nama = namaLengkapField.getText().trim();
    String alamat = alamatField.getText().trim();
    String telp = noTelpField.getText().trim();
    String email = emailField.getText().trim();

    if (username.isEmpty() || password.isEmpty() || nama.isEmpty() || alamat.isEmpty() || telp.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (password.length() < 6) {
        JOptionPane.showMessageDialog(this, "Password minimal 6 karakter!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!email.contains("@") || !email.contains(".")) {
        JOptionPane.showMessageDialog(this, "Email tidak valid!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        User user = new User(username, password, nama, alamat, telp, email, "pembayar");
        boolean berhasil = UserDAO.register(user);

        if (berhasil) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username sudah digunakan.", "Gagal", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
}