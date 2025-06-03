package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.*;

public class TambahPajakDialog extends JDialog {
    private JComboBox<String> cmbPembayar;
    private JTextField txtNomorPolisi;
    private JComboBox<String> cmbJenisKendaraan;
    private JTextField txtMerk;
    private JTextField txtType;
    private JSpinner spnTahun;
    private JTextField txtNomorSTNK;
    private JTextField txtNomorRangka;
    private JTextField txtNomorMesin;
    private JTextField txtPajak;
    private JTextField txtDenda;
    private JSpinner spnTanggalJatuhTempo;
    private JComboBox<String> cmbStatus;
    private JButton btnSimpan;
    private JButton btnBatal;
    
    private boolean dataSaved = false;
    private List<User> listPembayar;
    
    public TambahPajakDialog(JFrame parent) {
        super(parent, "Tambah Data Pajak Kendaraan", true);
        
        // Load data pembayar
        listPembayar = UserDAO.getAllPembayar();
        
        initComponents();
        setupLayout();
        setupEvents();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        // Combo Pembayar
        cmbPembayar = new JComboBox<>();
        for (User user : listPembayar) {
            cmbPembayar.addItem(user.getNamaLengkap() + " (" + user.getUsername() + ")");
        }
        
        txtNomorPolisi = new JTextField(20);
        
        // Jenis Kendaraan
        cmbJenisKendaraan = new JComboBox<>(new String[]{
            "Mobil", "Motor", "Truk", "Bus", "Pickup"
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
        txtDenda = new JTextField(20);
        txtDenda.setText("0");
        
        // Tanggal Jatuh Tempo
        spnTanggalJatuhTempo = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnTanggalJatuhTempo, "dd/MM/yyyy");
        spnTanggalJatuhTempo.setEditor(dateEditor);
        
        // Status
        cmbStatus = new JComboBox<>(new String[]{"BELUM_BAYAR", "LUNAS", "TERLAMBAT"});
        
        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("TAMBAH DATA PAJAK KENDARAAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Pembayar
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Pembayar *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cmbPembayar, gbc);
        row++;
        
        // Nomor Polisi
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor Polisi *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorPolisi, gbc);
        row++;
        
        // Jenis Kendaraan
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Jenis Kendaraan *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cmbJenisKendaraan, gbc);
        row++;
        
        // Merk
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Merk *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtMerk, gbc);
        row++;
        
        // Type
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Type *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtType, gbc);
        row++;
        
        // Tahun
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Tahun *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(spnTahun, gbc);
        row++;
        
        // Nomor STNK
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor STNK *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorSTNK, gbc);
        row++;
        
        // Nomor Rangka
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor Rangka *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorRangka, gbc);
        row++;
        
        // Nomor Mesin
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor Mesin *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorMesin, gbc);
        row++;
        
        // Pajak
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Pajak (Rp) *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtPajak, gbc);
        row++;
        
        // Denda
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Denda (Rp):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtDenda, gbc);
        row++;
        
        // Tanggal Jatuh Tempo
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Jatuh Tempo *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(spnTanggalJatuhTempo, gbc);
        row++;
        
        // Status
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Status *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cmbStatus, gbc);
        row++;
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpanData();
            }
        });
        
        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void simpanData() {
        try {
            // Validation
            if (cmbPembayar.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Pilih pembayar!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String nomorPolisi = txtNomorPolisi.getText().trim();
            String merk = txtMerk.getText().trim();
            String type = txtType.getText().trim();
            String nomorSTNK = txtNomorSTNK.getText().trim();
            String nomorRangka = txtNomorRangka.getText().trim();
            String nomorMesin = txtNomorMesin.getText().trim();
            String pajakStr = txtPajak.getText().trim();
            String dendaStr = txtDenda.getText().trim();
            
            if (nomorPolisi.isEmpty() || merk.isEmpty() || type.isEmpty() || 
                nomorSTNK.isEmpty() || nomorRangka.isEmpty() || nomorMesin.isEmpty() || pajakStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse numbers
            double pajak = Double.parseDouble(pajakStr);
            double denda = Double.parseDouble(dendaStr);
            
            if (pajak < 0 || denda < 0) {
                JOptionPane.showMessageDialog(this, "Pajak dan denda tidak boleh negatif!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get selected user
            User selectedUser = listPembayar.get(cmbPembayar.getSelectedIndex());
            
            // Create PajakKendaraan object
            PajakKendaraan pajak_obj = new PajakKendaraan();
            pajak_obj.setUserId(selectedUser.getId());
            pajak_obj.setNomorPolisi(nomorPolisi);
            pajak_obj.setJenisKendaraan((String) cmbJenisKendaraan.getSelectedItem());
            pajak_obj.setMerk(merk);
            pajak_obj.setType(type);
            pajak_obj.setTahunKendaraan((Integer) spnTahun.getValue());
            pajak_obj.setNomorSTNK(nomorSTNK);
            pajak_obj.setNomorRangka(nomorRangka);
            pajak_obj.setNomorMesin(nomorMesin);
            pajak_obj.setPajak(pajak);
            pajak_obj.setDenda(denda);
            
            // Parse date
            java.util.Date date = (java.util.Date) spnTanggalJatuhTempo.getValue();
            LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            pajak_obj.setTanggalJatuhTempo(localDate);
            
            pajak_obj.setStatus((String) cmbStatus.getSelectedItem());
            
            // Save to database
            if (PajakKendaraanDAO.tambah(pajak_obj)) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dataSaved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format angka tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isDataSaved() {
        return dataSaved;
    }
}