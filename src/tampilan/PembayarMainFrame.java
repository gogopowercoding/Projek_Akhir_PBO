package tampilan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import model.*;

public class PembayarMainFrame extends JFrame {
    private User currentUser;
    private JTable tablePajak;
    private DefaultTableModel tableModel;
    private JButton btnTambahKendaraan;
    private JButton btnEditKendaraan;
    private JButton btnUploadSTNK;
    private JButton btnBayarPajak;
    private JButton btnRefresh;
    private JButton btnLogout;
    private JButton btnProfile;
    private JLabel lblTotalTagihan;
    private JComboBox<String> cmbFilter;
    
    public PembayarMainFrame(User user) {
        this.currentUser = user;
        
        initComponents();
        setupLayout();
        setupEvents();
        refreshData();
        
        setTitle("Pembayar Pajak - Sistem Perpajakan Kendaraan Bermotor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Table
        String[] columns = {"ID", "Nomor Polisi", "Jenis", "Merk", "Type", "Tahun", 
                           "Pajak", "Denda", "Total", "Jatuh Tempo", "Status", "STNK"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePajak = new JTable(tableModel);
        tablePajak.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePajak.getTableHeader().setReorderingAllowed(false);
        
        // Buttons
        btnTambahKendaraan = new JButton("Tambah Kendaraan");
        btnEditKendaraan = new JButton("Edit Data");
        btnUploadSTNK = new JButton("Upload STNK");
        btnBayarPajak = new JButton("Bayar Pajak");
        btnRefresh = new JButton("Refresh");
        btnProfile = new JButton("Profile");
        btnLogout = new JButton("Logout");
        
        // Filter
        cmbFilter = new JComboBox<>(new String[]{"Semua Data", "Belum Bayar", "Sudah Lunas", "Terlambat"});
        
        // Label
        lblTotalTagihan = new JLabel("Total Tagihan: Rp 0");
        lblTotalTagihan.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalTagihan.setForeground(new Color(220, 20, 60));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("SISTEM PERPAJAKAN KENDARAAN BERMOTOR - PEMBAYAR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getNamaLengkap());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        controlPanel.add(new JLabel("Filter:"));
        controlPanel.add(cmbFilter);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnTambahKendaraan);
        controlPanel.add(btnEditKendaraan);
        controlPanel.add(btnUploadSTNK);
        controlPanel.add(btnBayarPajak);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnRefresh);
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(btnProfile);
        controlPanel.add(btnLogout);
        
        add(controlPanel, BorderLayout.NORTH);
        
        // Table Panel
        JScrollPane scrollPane = new JScrollPane(tablePajak);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Data Kendaraan Anda"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.add(lblTotalTagihan);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        btnTambahKendaraan.addActionListener(e -> openTambahKendaraanDialog());
        btnEditKendaraan.addActionListener(e -> editSelectedKendaraan());
        btnUploadSTNK.addActionListener(e -> uploadSTNK());
        btnBayarPajak.addActionListener(e -> bayarPajak());
        btnRefresh.addActionListener(e -> refreshData());
        btnProfile.addActionListener(e -> openProfileDialog());
        btnLogout.addActionListener(e -> logout());
        
        cmbFilter.addActionListener(e -> applyFilter());
        
        // Double click to view details
        tablePajak.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewDetails();
                }
            }
        });
    }
    
    private void refreshData() {
        // Update status terlambat terlebih dahulu
        PajakKendaraanDAO.updateStatusTerlambat();
        applyFilter();
    }
    
    private void applyFilter() {
        List<PajakKendaraan> dataList = PajakKendaraanDAO.getAllByUserId(currentUser.getId());
        String selectedFilter = (String) cmbFilter.getSelectedItem();
        
        switch (selectedFilter) {
            case "Belum Bayar":
                dataList = dataList.stream()
                    .filter(p -> "BELUM_BAYAR".equals(p.getStatus()))
                    .toList();
                break;
            case "Sudah Lunas":
                dataList = dataList.stream()
                    .filter(p -> "LUNAS".equals(p.getStatus()))
                    .toList();
                break;
            case "Terlambat":
                dataList = dataList.stream()
                    .filter(p -> "TERLAMBAT".equals(p.getStatus()))
                    .toList();
                break;
        }
        
        updateTable(dataList);
        updateTotalTagihan(dataList);
    }
    
    private void updateTable(List<PajakKendaraan> dataList) {
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        for (PajakKendaraan pajak : dataList) {
            String stnkStatus = (pajak.getPathGambarSTNK() != null && !pajak.getPathGambarSTNK().isEmpty()) 
                               ? "Uploaded" : "Not Uploaded";
            
            tableModel.addRow(new Object[]{
                pajak.getId(),
                pajak.getNomorPolisi(),
                pajak.getJenisKendaraan(),
                pajak.getMerk(),
                pajak.getType(),
                pajak.getTahunKendaraan(),
                currencyFormat.format(pajak.getPajak()),
                currencyFormat.format(pajak.getDenda()),
                currencyFormat.format(pajak.getTotalBayar()),
                pajak.getTanggalJatuhTempo(),
                pajak.getStatus(),
                stnkStatus
            });
        }
    }
    
    private void updateTotalTagihan(List<PajakKendaraan> dataList) {
        double total = dataList.stream()
            .filter(p -> !"LUNAS".equals(p.getStatus()))
            .mapToDouble(PajakKendaraan::getTotalBayar)
            .sum();
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        lblTotalTagihan.setText("Total Tagihan Anda: " + currencyFormat.format(total));
    }
    
    private void openTambahKendaraanDialog() {
        TambahKendaraanDialog dialog = new TambahKendaraanDialog(this, currentUser);
        dialog.setVisible(true);
        if (dialog.isDataSaved()) {
            refreshData();
        }
    }
    
    private void editSelectedKendaraan() {
        int selectedRow = tablePajak.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data yang akan diedit!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int pajakId = (Integer) tableModel.getValueAt(selectedRow, 0);
        EditKendaraanDialog dialog = new EditKendaraanDialog(this, pajakId, currentUser);
        dialog.setVisible(true);
        if (dialog.isDataSaved()) {
            refreshData();
        }
    }
    
    private void uploadSTNK() {
        int selectedRow = tablePajak.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih kendaraan untuk upload STNK!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int pajakId = (Integer) tableModel.getValueAt(selectedRow, 0);
        UploadSTNKDialog dialog = new UploadSTNKDialog(this, pajakId);
        dialog.setVisible(true);
        if (dialog.isFileUploaded()) {
            refreshData();
        }
    }
    
    private void bayarPajak() {
        int selectedRow = tablePajak.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih pajak yang akan dibayar!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String status = (String) tableModel.getValueAt(selectedRow, 10);
        if ("LUNAS".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "Pajak ini sudah lunas!", 
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int pajakId = (Integer) tableModel.getValueAt(selectedRow, 0);
        BayarPajakDialog dialog = new BayarPajakDialog(this, pajakId);
        dialog.setVisible(true);
        if (dialog.isPaid()) {
            refreshData();
        }
    }
    
    private void viewDetails() {
        int selectedRow = tablePajak.getSelectedRow();
        if (selectedRow == -1) return;
        
        int pajakId = (Integer) tableModel.getValueAt(selectedRow, 0);
        DetailKendaraanDialog dialog = new DetailKendaraanDialog(this, pajakId);
        dialog.setVisible(true);
    }
    
    private void openProfileDialog() {
        ProfileDialog dialog = new ProfileDialog(this, currentUser);
        dialog.setVisible(true);
        if (dialog.isDataUpdated()) {
            // Refresh user data if needed
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin logout?", 
            "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}