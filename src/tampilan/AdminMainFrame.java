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

public class AdminMainFrame extends JFrame {
    private User currentUser;
    private JTable tablePajak;
    private DefaultTableModel tableModel;
    private JButton btnTambahPajak;
    private JButton btnEditPajak;
    private JButton btnHapusPajak;
    private JButton btnProsesPayment;
    private JButton btnRefresh;
    private JButton btnLogout;
    private JLabel lblTotalTagihan;
    private JComboBox<String> cmbFilter;
    
    public AdminMainFrame(User user) {
        this.currentUser = user;
        
        initComponents();
        setupLayout();
        setupEvents();
        refreshData();
        
        setTitle("Admin Panel - Sistem Perpajakan Kendaraan Bermotor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Table
        String[] columns = {"ID", "Nomor Polisi", "Jenis", "Merk", "Type", "Tahun", 
                           "Pemilik", "Pajak", "Denda", "Total", "Jatuh Tempo", "Status"};
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
        btnTambahPajak = new JButton("Tambah Data Pajak");
        btnEditPajak = new JButton("Edit Data");
        btnHapusPajak = new JButton("Hapus Data");
        btnProsesPayment = new JButton("Proses Pembayaran");
        btnRefresh = new JButton("Refresh");
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
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("ADMIN PANEL - SISTEM PERPAJAKAN KENDARAAN BERMOTOR");
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
        controlPanel.add(btnTambahPajak);
        controlPanel.add(btnEditPajak);
        controlPanel.add(btnHapusPajak);
        controlPanel.add(btnProsesPayment);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(btnRefresh);
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(btnLogout);
        
        add(controlPanel, BorderLayout.NORTH);
        
        // Table Panel
        JScrollPane scrollPane = new JScrollPane(tablePajak);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Data Pajak Kendaraan"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.add(lblTotalTagihan);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        btnTambahPajak.addActionListener(e -> openTambahPajakDialog());
        btnEditPajak.addActionListener(e -> editSelectedPajak());
        btnHapusPajak.addActionListener(e -> hapusSelectedPajak());
        btnProsesPayment.addActionListener(e -> prosesPayment());
        btnRefresh.addActionListener(e -> refreshData());
        btnLogout.addActionListener(e -> logout());
        
        cmbFilter.addActionListener(e -> applyFilter());
        
        // Double click to edit
        tablePajak.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedPajak();
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
        List<PajakKendaraan> dataList;
        String selectedFilter = (String) cmbFilter.getSelectedItem();
        
        switch (selectedFilter) {
            case "Belum Bayar":
                dataList = PajakKendaraanDAO.getBelumBayar();
                break;
            case "Sudah Lunas":
                dataList = PajakKendaraanDAO.getAll().stream()
                    .filter(p -> "LUNAS".equals(p.getStatus()))
                    .toList();
                break;
            case "Terlambat":
                dataList = PajakKendaraanDAO.getAll().stream()
                    .filter(p -> "TERLAMBAT".equals(p.getStatus()))
                    .toList();
                break;
            default:
                dataList = PajakKendaraanDAO.getAll();
                break;
        }
        
        updateTable(dataList);
        updateTotalTagihan(dataList);
    }
    
    private void updateTable(List<PajakKendaraan> dataList) {
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        for (PajakKendaraan pajak : dataList) {
            // Get owner name
            String pemilik = getUserNameById(pajak.getUserId());
            
            tableModel.addRow(new Object[]{
                pajak.getId(),
                pajak.getNomorPolisi(),
                pajak.getJenisKendaraan(),
                pajak.getMerk(),
                pajak.getType(),
                pajak.getTahunKendaraan(),
                pemilik,
                currencyFormat.format(pajak.getPajak()),
                currencyFormat.format(pajak.getDenda()),
                currencyFormat.format(pajak.getTotalBayar()),
                pajak.getTanggalJatuhTempo(),
                pajak.getStatus()
            });
        }
    }
    
    private void updateTotalTagihan(List<PajakKendaraan> dataList) {
        double total = dataList.stream()
            .filter(p -> !"LUNAS".equals(p.getStatus()))
            .mapToDouble(PajakKendaraan::getTotalBayar)
            .sum();
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        lblTotalTagihan.setText("Total Tagihan Belum Lunas: " + currencyFormat.format(total));
    }
    
    private String getUserNameById(int userId) {
        // Simple implementation - in real app, you might want to cache this
        List<User> users = UserDAO.getAllPembayar();
        return users.stream()
            .filter(u -> u.getId() == userId)
            .map(User::getNamaLengkap)
            .findFirst()
            .orElse("Unknown");
    }
    
    private void openTambahPajakDialog() {
        TambahPajakDialog dialog = new TambahPajakDialog(this);
        dialog.setVisible(true);
        if (dialog.isDataSaved()) {
            refreshData();
        }
    }
    
    private void editSelectedPajak() {
        int selectedRow = tablePajak.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data yang akan diedit!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int pajakId = (Integer) tableModel.getValueAt(selectedRow, 0);
        EditPajakDialog dialog = new EditPajakDialog(this, pajakId);
        dialog.setVisible(true);
        if (dialog.isDataSaved()) {
            refreshData();
        }
    }
    
    private void hapusSelectedPajak() {
        int selectedRow = tablePajak.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data yang akan dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data ini?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int pajakId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (PajakKendaraanDAO.hapus(pajakId)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void prosesPayment() {
        int selectedRow = tablePajak.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data yang akan diproses pembayarannya!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String status = (String) tableModel.getValueAt(selectedRow, 11);
        if ("LUNAS".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "Pajak ini sudah lunas!", 
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Konfirmasi pembayaran pajak ini?", 
            "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int pajakId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (PajakKendaraanDAO.bayar(pajakId)) {
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil diproses!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memproses pembayaran!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
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