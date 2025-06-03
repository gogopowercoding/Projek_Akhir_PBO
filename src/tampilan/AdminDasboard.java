package tampilan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.User;
import model.UserDAO;
import basisdata.KoneksiBasisData;

class AdminDashboard extends JFrame {
    private JTable pajakTable;
    
    public AdminDashboard() {
        setTitle("Dashboard Admin - Sistem Pajak Kendaraan");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu dataMenu = new JMenu("Data");
        JMenu laporanMenu = new JMenu("Laporan");
        
        JMenuItem dataPajakItem = new JMenuItem("Data Pajak");
        JMenuItem dataPembayarItem = new JMenuItem("Data Pembayar");
        JMenuItem laporanPajakItem = new JMenuItem("Laporan Pajak");
        
        dataMenu.add(dataPajakItem);
        dataMenu.add(dataPembayarItem);
        laporanMenu.add(laporanPajakItem);
        
        menuBar.add(dataMenu);
        menuBar.add(laporanMenu);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(logoutButton);
        
        setJMenuBar(menuBar);
        
        // Main Panel
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("DASHBOARD ADMIN"));
        add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Nomor Polisi", "Pemilik", "Jenis", "Merk", "Pajak", "Status", "Jatuh Tempo"};
        Object[][] data = {
            {1, "B 1234 CD", "John Doe", "Mobil", "Toyota", "Rp 500.000", "BELUM BAYAR", "2024-01-15"},
            {2, "B 5678 EF", "Jane Smith", "Motor", "Honda", "Rp 150.000", "LUNAS", "2024-02-10"}
        };
        
        pajakTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(pajakTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton tambahButton = new JButton("Tambah Data");
        JButton editButton = new JButton("Edit");
        JButton hapusButton = new JButton("Hapus");
        JButton refreshButton = new JButton("Refresh");
        
        tambahButton.addActionListener(e -> new TambahPajakFrame().setVisible(true));
        
        buttonPanel.add(tambahButton);
        buttonPanel.add(editButton);
        buttonPanel.add(hapusButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
