package tampilan;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import model.PajakKendaraan;
import model.PajakKendaraanDAO;
import model.UserDAO;

public class DetailKendaraanDialog extends JDialog {
    private int pajakId;

    public DetailKendaraanDialog(JFrame parent, int pajakId) {
        super(parent, "Detail Kendaraan", true);
        this.pajakId = pajakId;

        setupLayout();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("DETAIL KENDARAAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        PajakKendaraan pajak = PajakKendaraanDAO.getAll().stream()
                .filter(p -> p.getId() == pajakId)
                .findFirst()
                .orElse(null);

        if (pajak != null) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String pemilik = UserDAO.getAllPembayar().stream()
                    .filter(u -> u.getId() == pajak.getUserId())
                    .map(User::getNamaLengkap)
                    .findFirst()
                    .orElse("Tidak Diketahui");

            int row = 0;
            addDetailRow(mainPanel, gbc, row++, "Pemilik:", pemilik);
            addDetailRow(mainPanel, gbc, row++, "Nomor Polisi:", pajak.getNomorPolisi());
            addDetailRow(mainPanel, gbc, row++, "Jenis Kendaraan:", pajak.getJenisKendaraan());
            addDetailRow(mainPanel, gbc, row++, "Merk:", pajak.getMerk());
            addDetailRow(mainPanel, gbc, row++, "Type:", pajak.getType());
            addDetailRow(mainPanel, gbc, row++, "Tahun:", String.valueOf(pajak.getTahunKendaraan()));
            addDetailRow(mainPanel, gbc, row++, "Nomor STNK:", pajak.getNomorSTNK());
            addDetailRow(mainPanel, gbc, row++, "Nomor Rangka:", pajak.getNomorRangka());
            addDetailRow(mainPanel, gbc, row++, "Nomor Mesin:", pajak.getNomorMesin());
            addDetailRow(mainPanel, gbc, row++, "Pajak:", currencyFormat.format(pajak.getPajak()));
            addDetailRow(mainPanel, gbc, row++, "Denda:", currencyFormat.format(pajak.getDenda()));
            addDetailRow(mainPanel, gbc, row++, "Total Bayar:", currencyFormat.format(pajak.getTotalBayar()));
            addDetailRow(mainPanel, gbc, row++, "Tanggal Jatuh Tempo:", pajak.getTanggalJatuhTempo().toString());
            addDetailRow(mainPanel, gbc, row++, "Status:", pajak.getStatus());
            addDetailRow(mainPanel, gbc, row++, "STNK:", pajak.getPathGambarSTNK() != null ? "Sudah Diunggah" : "Belum Diunggah");
        }

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());
        buttonPanel.add(btnTutup);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
    }
}