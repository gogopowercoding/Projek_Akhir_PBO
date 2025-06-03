package tampilan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import model.PajakKendaraan;
import model.PajakKendaraanDAO;

public class BayarPajakDialog extends JDialog {
    private int pajakId;
    private JLabel lblNomorPolisi;
    private JLabel lblTotalBayar;
    private JComboBox<String> cmbMetodePembayaran;
    private JButton btnBayar;
    private JButton btnBatal;
    private boolean isPaid = false;

    public BayarPajakDialog(JFrame parent, int pajakId) {
        super(parent, "Bayar Pajak", true);
        this.pajakId = pajakId;

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
        lblNomorPolisi = new JLabel("Nomor Polisi: ");
        lblTotalBayar = new JLabel("Total Bayar: Rp 0");
        cmbMetodePembayaran = new JComboBox<>(new String[]{"Transfer Bank", "Kartu Kredit", "E-Wallet"});
        btnBayar = new JButton("Bayar");
        btnBatal = new JButton("Batal");
    }

    private void loadData() {
        PajakKendaraan pajak = PajakKendaraanDAO.getAll().stream()
                .filter(p -> p.getId() == pajakId)
                .findFirst()
                .orElse(null);

        if (pajak != null) {
            lblNomorPolisi.setText("Nomor Polisi: " + pajak.getNomorPolisi());
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            lblTotalBayar.setText("Total Bayar: " + currencyFormat.format(pajak.getTotalBayar()));
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("BAYAR PAJAK KENDARAAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(lblNomorPolisi, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(lblTotalBayar, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Metode Pembayaran *:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(cmbMetodePembayaran, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnBayar);
        buttonPanel.add(btnBatal);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEvents() {
        btnBayar.addActionListener(e -> bayarPajak());
        btnBatal.addActionListener(e -> dispose());
    }

    private void bayarPajak() {
        if (PajakKendaraanDAO.bayar(pajakId)) {
            JOptionPane.showMessageDialog(this, "Pembayaran berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            isPaid = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memproses pembayaran!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isPaid() {
        return isPaid;
    }
}