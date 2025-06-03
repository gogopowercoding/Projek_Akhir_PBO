package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;
import model.PajakKendaraan;
import model.PajakKendaraanDAO;
import model.User;

public class EditKendaraanDialog extends JDialog {
    private int pajakId;
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

    public EditKendaraanDialog(JFrame parent, int pajakId, User user) {
        super(parent, "Edit Data Kendaraan", true);
        this.pajakId = pajakId;
        this.currentUser = user;

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
        txtNomorPolisi = new JTextField(20);
        cmbJenisKendaraan = new JComboBox<>(new String[]{"Motor", "Mobil", "Truk", "Bus", "Pickup"});
        txtMerk = new JTextField(20);
        txtType = new JTextField(20);
        spnTahun = new JSpinner(new SpinnerNumberModel(2024, 1980, 2030, 1));
        txtNomorSTNK = new JTextField(20);
        txtNomorRangka = new JTextField(20);
        txtNomorMesin = new JTextField(20);
        txtPajak = new JTextField(20);
        spnTanggalJatuhTempo = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnTanggalJatuhTempo, "dd/MM/yyyy");
        spnTanggalJatuhTempo.setEditor(dateEditor);
        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");
    }

    private void loadData() {
        PajakKendaraan pajak = PajakKendaraanDAO.getAllByUserId(currentUser.getId()).stream()
                .filter(p -> p.getId() == pajakId)
                .findFirst()
                .orElse(null);

        if (pajak != null) {
            txtNomorPolisi.setText(pajak.getNomorPolisi());
            cmbJenisKendaraan.setSelectedItem(pajak.getJenisKendaraan());
            txtMerk.setText(pajak.getMerk());
            txtType.setText(pajak.getType());
            spnTahun.setValue(pajak.getTahunKendaraan());
            txtNomorSTNK.setText(pajak.getNomorSTNK());
            txtNomorRangka.setText(pajak.getNomorRangka());
            txtNomorMesin.setText(pajak.getNomorMesin());
            txtPajak.setText(String.valueOf(pajak.getPajak()));
            spnTanggalJatuhTempo.setValue(Date.from(pajak.getTanggalJatuhTempo().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("EDIT DATA KENDARAAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nomor Polisi *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNomorPolisi, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Jenis Kendaraan *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cmbJenisKendaraan, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Merk *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMerk, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Type *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtType, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tahun *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(spnTahun, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nomor STNK *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNomorSTNK, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nomor Rangka *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNomorRangka, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nomor Mesin *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNomorMesin, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Pajak (Rp) *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPajak, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tanggal Jatuh Tempo *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(spnTanggalJatuhTempo, gbc);
        row++;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void setupEvents() {
        btnSimpan.addActionListener(e -> simpanData());
        btnBatal.addActionListener(e -> dispose());
    }

    private void simpanData() {
        try {
            String nomorPolisi = txtNomorPolisi.getText().trim();
            String merk = txtMerk.getText().trim();
            String type = txtType.getText().trim();
            String nomorSTNK = txtNomorSTNK.getText().trim();
            String nomorRangka = txtNomorRangka.getText().trim();
            String nomorMesin = txtNomorMesin.getText().trim();
            String pajakStr = txtPajak.getText().trim();

            if (nomorPolisi.isEmpty() || merk.isEmpty() || type.isEmpty() ||
                    nomorSTNK.isEmpty() || nomorRangka.isEmpty() || nomorMesin.isEmpty() || pajakStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double pajak = Double.parseDouble(pajakStr);
            if (pajak < 0) {
                JOptionPane.showMessageDialog(this, "Pajak tidak boleh negatif!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PajakKendaraan pajakObj = new PajakKendaraan();
            pajakObj.setId(pajakId);
            pajakObj.setUserId(currentUser.getId());
            pajakObj.setNomorPolisi(nomorPolisi);
            pajakObj.setJenisKendaraan((String) cmbJenisKendaraan.getSelectedItem());
            pajakObj.setMerk(merk);
            pajakObj.setType(type);
            pajakObj.setTahunKendaraan((Integer) spnTahun.getValue());
            pajakObj.setNomorSTNK(nomorSTNK);
            pajakObj.setNomorRangka(nomorRangka);
            pajakObj.setNomorMesin(nomorMesin);
            pajakObj.setPajak(pajak);
            java.util.Date date = (java.util.Date) spnTanggalJatuhTempo.getValue();
            LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            pajakObj.setTanggalJatuhTempo(localDate);
            pajakObj.setStatus("BELUM_BAYAR");

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