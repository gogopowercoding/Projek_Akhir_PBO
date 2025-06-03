package tampilan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.User;
import model.UserDAO;
import basisdata.KoneksiBasisData;

class UserDashboard extends JFrame {
    private JTable myPajakTable;
    
    public UserDashboard() {
        setTitle("Dashboard Pembayar - Sistem Pajak Kendaraan");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Selamat Datang, User!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(logoutButton);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Nomor Polisi", "Jenis", "Merk", "Pajak", "Denda", "Total", "Status", "Jatuh Tempo"};
        Object[][] data = {
            {"B 9999 XY", "Mobil", "Honda", "Rp 500.000", "Rp 0", "Rp 500.000", "BELUM BAYAR", "2024-03-01"}
        };
        
        myPajakTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(myPajakTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton bayarButton = new JButton("Bayar Pajak");
        JButton detailButton = new JButton("Detail");
        JButton refreshButton = new JButton("Refresh");
        
        bayarButton.addActionListener(e -> {
            int selectedRow = myPajakTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Konfirmasi pembayaran pajak?", "Konfirmasi", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Pembayaran berhasil!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih pajak yang akan dibayar!");
            }
        });
        
        buttonPanel.add(bayarButton);
        buttonPanel.add(detailButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

