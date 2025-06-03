package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import model.PajakKendaraan;
import model.PajakKendaraanDAO;
import model.User;
import model.UserDAO;

public class EditPajakDialog extends JDialog {
    private int pajakId;
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

    public EditPajakDialog(JFrame parent, int pajakId) {
        super(parent, "Edit Data Pajak Kendaraan", true);
        this.pajakId = pajakId;

        listPembayar = UserDAO.getAllPembayar();
        initComponents();
        loadData();
        setupLayout();
        setupEvents();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        cmbPembayar = new JComboBox<>();
        for (User user : listPembayar) {
            cmbPembayar.addItem(user.getNamaLengkap() + " (" + user.getUsername() + ")");
        }

        txtNomorPolisi = new JTextField(20);
        cmbJenisKendaraan = new JComboBox<>(new String[]{"Mobil", "Motor", "Truk", "Bus", "Pickup"});
        txtMerk = new JTextField(20);
        txtType = new JTextField(20);
        spnTahun = new JSpinner(new SpinnerNumberModel(2024, 1980, 2030, 1));
        txtNomorSTNK = new JTextField(20);
        txtNomorRangka = new JTextField(20);
        txtNomorMesin = new JTextField(20);
        txtPajak = new JTextField(20);
        txtDenda = new JTextField(20);
        spnTanggalJatuhTempo = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnTanggalJatuhTempo, "dd/MM/yyyy");
        spnTanggalJatuhTempo.setEditor(dateEditor);
        cmbStatus = new JComboBox<>(new String[]{"BELUM_BAYAR", "LUNAS", "TERLAMBAT"});

        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");
    }

    private void loadData() {
        PajakKendaraan pajak = PajakKendaraanDAO.getAll().stream()
                .filter(p -> p.getId() == pajakId)
                .findFirst()
                .orElse(null);

        if (pajak != null) {
            for (int i = 0; i < listPembayar.size(); i++) {
                if (listPembayar.get(i).getId() == pajak.getUserId()) {
                    cmbPembayar.setSelectedIndex(i);
                    break;
                }
            }
            txtNomorPolisi.setText(pajak.getNomorPolisi());
            cmbJenisKendaraan.setSelectedItem(pajak.getJenisKendaraan());
            txtMerk.setText(pajak.getMerk());
            txtType.setText(pajak.getType());
            spnTahun.setValue(pajak.getTahunKendaraan());
            txtNomorSTNK.setText(pajak.getNomorSTNK());
            txtNomorRangka.setText(pajak.getNomorRangka());
            txtNomorMesin.setText(pajak.getNomorMesin());
            txtPajak.setText(String.valueOf(pajak.getPajak()));
            txtDenda.setText(String.valueOf(pajak.getDenda()));
            spnTanggalJatuhTempo.setValue(Date.from(pajak.getTanggalJatuhTempo().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
            cmbStatus.setSelectedItem(pajak.getStatus());
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("EDIT DATA PAJAK KENDARAAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Pembayar *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cmbPembayar, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor Polisi *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorPolisi, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Jenis Kendaraan *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cmbJenisKendaraan, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Merk *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtMerk, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Type *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtType, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Tahun *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(spnTahun, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor STNK *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorSTNK, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor Rangka *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorRangka, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nomor Mesin *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtNomorMesin, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Pajak (Rp) *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtPajak, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Denda (Rp):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(txtDenda, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Jatuh Tempo *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(spnTanggalJatuhTempo, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Status *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cmbStatus, gbc);
        row++;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEvents() {
        btnSimpan.addActionListener(e -> simpanData());
        btnBatal.addActionListener(e -> dispose());
    }

    private void simpanData() {
        try {
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

            double pajak = Double.parseDouble(pajakStr);
            double denda = dendaStr.isEmpty() ? 0 : Double.parseDouble(dendaStr);

            if (pajak < 0 || denda < 0) {
                JOptionPane.showMessageDialog(this, "Pajak dan denda tidak boleh negatif!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User selectedUser = listPembayar.get(cmbPembayar.getSelectedIndex());
            PajakKendaraan pajakObj = new PajakKendaraan();
            pajakObj.setId(pajakId);
            pajakObj.setUserId(selectedUser.getId());
            pajakObj.setNomorPolisi(nomorPolisi);
            pajakObj.setJenisKendaraan((String) cmbJenisKendaraan.getSelectedItem());
            pajakObj.setMerk(merk);
            pajakObj.setType(type);
            pajakObj.setTahunKendaraan((Integer) spnTahun.getValue());
            pajakObj.setNomorSTNK(nomorSTNK);
            pajakObj.setNomorRangka(nomorRangka);
            pajakObj.setNomorMesin(nomorMesin);
            pajakObj.setPajak(pajak);
            pajakObj.setDenda(denda);
            java.util.Date date = (java.util.Date) spnTanggalJatuhTempo.getValue();
            LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            pajakObj.setTanggalJatuhTempo(localDate);
            pajakObj.setStatus((String) cmbStatus.getSelectedItem());

            if (PajakKendaraanDAO.update(pajakObj)) {
                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dataSaved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui data!", "Error", JOptionPane.ERROR_MESSAGE);
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