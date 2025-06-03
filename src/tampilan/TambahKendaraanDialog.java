package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
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
        headerPanel.setBackground(new Color(34, 139,