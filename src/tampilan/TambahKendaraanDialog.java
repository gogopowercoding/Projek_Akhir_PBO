package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import model.*;

public class TambahKendaraanDialog extends JDialog {
    private User currentUser;
    private JTextField txtNomorPolisi;
    private JComboBox<String> cmbJenisKendaraan;
    private JTextField txtMerk;
    private JTextField txtType;
    private JSpinner spnTahun;
    private JTextField txtNomorSTNK;
    private JTextField txtNomorRangka;
    private JTextField txtNomorMesin;
    private JTextField txtPajak;
    private JSpinner spnTanggalJatuhTempo;
    private JButton btnSimpan;
    private JButton btnBatal;
    
    private boolean dataSaved = false;
    
    public TambahKendaraanDialog(JFrame parent, User user) {
        super(parent, "Tambah Kendaraan Baru", true);
        this.currentUser = user;
        
        initComponents();
        setupLayout();
        setupEvents();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        txtNomorPolisi = new JTextField(20);
        
        // Jenis Kendaraan
        cmbJenisKendaraan = new JComboBox<>(new String[]{
            "Motor", "Mobil", "Truk", "Bus", "Pickup"
        });
        
        txtMerk = new JTextField(20);
        txtType = new JTextField(20);
        
        // Tahun
        spnTahun = new JSpinner(new SpinnerNumberModel(2024, 1980, 2030, 1));
        
        txtNomorSTNK = new JTextField(20);
        txtNomorRangka = new JTextField(20);
        txtNomorMesin = new JTextField(20);
        
        txtPajak = new JTextField(20);
        txtPajak.setText("0");
        
        // Tanggal Jatuh Tempo (default 1 tahun dari sekarang)
        spnTanggalJatuhTempo = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnTanggalJatuhTempo, "dd/MM/yyyy");
        spnTanggalJatuhTempo.setEditor(dateEditor);
        
        // Set default date to 1 year from now
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.YEAR, 1);
        spnTanggalJatuhTempo.setValue(cal.getTime());
        
        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("Tambah Kendaraan Baru");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 0 - Nomor Polisi
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nomor Polisi:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtNomorPolisi, gbc);
        
        // Row 1 - Jenis Kendaraan
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Jenis Kendaraan:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(cmbJenisKendaraan, gbc);
        
        // Row 2 - Merk
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Merk:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtMerk, gbc);
        
        // Row 3 - Type
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtType, gbc);
        
        // Row 4 - Tahun
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Tahun:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(spnTahun, gbc);
        
        // Row 5 - Nomor STNK
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Nomor STNK:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtNomorSTNK, gbc);
        
        // Row 6 - Nomor Rangka
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Nomor Rangka:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtNomorRangka, gbc);
        
        // Row 7 - Nomor Mesin
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Nomor Mesin:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtNomorMesin, gbc);
        
        // Row 8 - Pajak
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Pajak (Rp):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtPajak, gbc);
        
        // Row 9 - Tanggal Jatuh Tempo
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Tanggal Jatuh Tempo:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(spnTanggalJatuhTempo, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        btnBatal.setPreferredSize(new Dimension(80, 30));
        btnSimpan.setPreferredSize(new Dimension(80, 30));
        
        buttonPanel.add(btnBatal);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(btnSimpan);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpanKendaraan();
            }
        });
        
        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Format input untuk nomor polisi
        txtNomorPolisi.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String text = txtNomorPolisi.getText().toUpperCase();
                txtNomorPolisi.setText(text);
            }
        });
        
        // Format input untuk pajak (hanya angka)
        txtPajak.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }
    
    private void simpanKendaraan() {
        // Validasi input
        if (!validateInput()) {
            return;
        }
        
        try {
            // Buat objek kendaraan baru
            Kendaraan kendaraan = new Kendaraan();
            kendaraan.setNomorPolisi(txtNomorPolisi.getText().trim().toUpperCase());
            kendaraan.setJenisKendaraan(cmbJenisKendaraan.getSelectedItem().toString());
            kendaraan.setMerk(txtMerk.getText().trim());
            kendaraan.setType(txtType.getText().trim());
            kendaraan.setTahun((Integer) spnTahun.getValue());
            kendaraan.setNomorSTNK(txtNomorSTNK.getText().trim());
            kendaraan.setNomorRangka(txtNomorRangka.getText().trim());
            kendaraan.setNomorMesin(txtNomorMesin.getText().trim());
            
            // Parse pajak
            double pajak = 0;
            try {
                pajak = Double.parseDouble(txtPajak.getText().trim());
            } catch (NumberFormatException ex) {
                pajak = 0;
            }
            kendaraan.setPajak(pajak);
            
            // Convert Date to LocalDate
            Date selectedDate = (Date) spnTanggalJatuhTempo.getValue();
            LocalDate tanggalJatuhTempo = selectedDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            kendaraan.setTanggalJatuhTempo(tanggalJatuhTempo);
            
            // Set user ID
            kendaraan.setUserId(currentUser.getId());
            
            // Save to database (implement database saving logic here)
            // KendaraanDAO.save(kendaraan);
            
            dataSaved = true;
            
            JOptionPane.showMessageDialog(this, 
                "Kendaraan berhasil ditambahkan!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Terjadi kesalahan saat menyimpan data: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateInput() {
        // Validasi nomor polisi
        if (txtNomorPolisi.getText().trim().isEmpty()) {
            showValidationError("Nomor polisi harus diisi!");
            txtNomorPolisi.requestFocus();
            return false;
        }
        
        // Validasi merk
        if (txtMerk.getText().trim().isEmpty()) {
            showValidationError("Merk kendaraan harus diisi!");
            txtMerk.requestFocus();
            return false;
        }
        
        // Validasi type
        if (txtType.getText().trim().isEmpty()) {
            showValidationError("Type kendaraan harus diisi!");
            txtType.requestFocus();
            return false;
        }
        
        // Validasi nomor STNK
        if (txtNomorSTNK.getText().trim().isEmpty()) {
            showValidationError("Nomor STNK harus diisi!");
            txtNomorSTNK.requestFocus();
            return false;
        }
        
        // Validasi nomor rangka
        if (txtNomorRangka.getText().trim().isEmpty()) {
            showValidationError("Nomor rangka harus diisi!");
            txtNomorRangka.requestFocus();
            return false;
        }
        
        // Validasi nomor mesin
        if (txtNomorMesin.getText().trim().isEmpty()) {
            showValidationError("Nomor mesin harus diisi!");
            txtNomorMesin.requestFocus();
            return false;
        }
        
        // Validasi format nomor polisi (contoh sederhana)
        String nomorPolisi = txtNomorPolisi.getText().trim();
        if (nomorPolisi.length() < 3) {
            showValidationError("Format nomor polisi tidak valid!");
            txtNomorPolisi.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Validasi Error", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    public boolean isDataSaved() {
        return dataSaved;
    }
    
    // Method untuk mendapatkan data kendaraan yang sudah disimpan
    public Kendaraan getKendaraanData() {
        if (!dataSaved) return null;
        
        Kendaraan kendaraan = new Kendaraan();
        kendaraan.setNomorPolisi(txtNomorPolisi.getText().trim().toUpperCase());
        kendaraan.setJenisKendaraan(cmbJenisKendaraan.getSelectedItem().toString());
        kendaraan.setMerk(txtMerk.getText().trim());
        kendaraan.setType(txtType.getText().trim());
        kendaraan.setTahun((Integer) spnTahun.getValue());
        kendaraan.setNomorSTNK(txtNomorSTNK.getText().trim());
        kendaraan.setNomorRangka(txtNomorRangka.getText().trim());
        kendaraan.setNomorMesin(txtNomorMesin.getText().trim());
        
        double pajak = 0;
        try {
            pajak = Double.parseDouble(txtPajak.getText().trim());
        } catch (NumberFormatException ex) {
            pajak = 0;
        }
        kendaraan.setPajak(pajak);
        
        Date selectedDate = (Date) spnTanggalJatuhTempo.getValue();
        LocalDate tanggalJatuhTempo = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        kendaraan.setTanggalJatuhTempo(tanggalJatuhTempo);
        kendaraan.setUserId(currentUser.getId());
        
        return kendaraan;
    }
}