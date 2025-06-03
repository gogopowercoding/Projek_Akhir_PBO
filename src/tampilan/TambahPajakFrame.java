package tampilan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.User;
import model.UserDAO;
import basisdata.KoneksiBasisData;

class TambahPajakFrame extends JFrame {
    private JTextField nomorPolisiField, merkField, typeField, tahunField;
    private JTextField nomorSTNKField, nomorRangkaField, nomorMesinField, pajakField;
    private JComboBox<String> jenisCombo, pembayarCombo;
    
    public TambahPajakFrame() {
        setTitle("Tambah Data Pajak");
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("TAMBAH DATA PAJAK KENDARAAN", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        String[] labels = {"Pembayar:", "Nomor Polisi:", "Jenis Kendaraan:", "Merk:", 
                          "Type:", "Tahun:", "Nomor STNK:", "Nomor Rangka:", 
                          "Nomor Mesin:", "Pajak (Rp):"};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            if (i == 0) {
                pembayarCombo = new JComboBox<>(new String[]{"Pilih Pembayar", "John Doe", "Jane Smith"});
                formPanel.add(pembayarCombo, gbc);
            } else if (i == 2) {
                jenisCombo = new JComboBox<>(new String[]{"Mobil", "Motor"});
                formPanel.add(jenisCombo, gbc);
            } else {
                JTextField field = new JTextField(20);
                formPanel.add(field, gbc);
                
                // Assign references
                switch(i) {
                    case 1: nomorPolisiField = field; break;
                    case 3: merkField = field; break;
                    case 4: typeField = field; break;
                    case 5: tahunField = field; break;
                    case 6: nomorSTNKField = field; break;
                    case 7: nomorRangkaField = field; break;
                    case 8: nomorMesinField = field; break;
                    case 9: pajakField = field; break;
                }
            }
        }
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton simpanButton = new JButton("Simpan");
        JButton batalButton = new JButton("Batal");
        
        simpanButton.addActionListener(e -> {
            // Implementasi penyimpanan data
            JOptionPane.showMessageDialog(this, "Data pajak berhasil disimpan!");
            dispose();
        });
        
        batalButton.addActionListener(e -> dispose());
        
        buttonPanel.add(simpanButton);
        buttonPanel.add(batalButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
