package basisdata;

import java.sql.*;
import javax.swing.JOptionPane;

public class KoneksiBasisData {
    private static final String URL = "jdbc:mysql://localhost:3306/pajak_kendaraan?useSSL=false&autoReconnect=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";
    
    public static Connection getKoneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection koneksi = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi berhasil dibuat");
            return koneksi;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Driver MySQL tidak ditemukan: " + e.getMessage(), 
                "Error Driver", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Gagal koneksi ke database:\n" + e.getMessage(), 
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    // Method untuk membuat tabel jika belum ada
    public static void createTablesIfNotExists() {
        try (Connection conn = getKoneksi();
             Statement stmt = conn.createStatement()) {
            
            // Tabel users
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    nama_lengkap VARCHAR(100) NOT NULL,
                    alamat TEXT,
                    no_telp VARCHAR(20),
                    email VARCHAR(100),
                    role ENUM('admin', 'pembayar') NOT NULL DEFAULT 'pembayar',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.executeUpdate(createUsersTable);
            
            // Tabel pajak_kendaraan
            String createPajakTable = """
                CREATE TABLE IF NOT EXISTS pajak_kendaraan (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    nomor_polisi VARCHAR(15) NOT NULL,
                    jenis_kendaraan VARCHAR(50) NOT NULL,
                    merk VARCHAR(50) NOT NULL,
                    type VARCHAR(50) NOT NULL,
                    tahun_kendaraan INT NOT NULL,
                    nomor_stnk VARCHAR(50) NOT NULL,
                    nomor_rangka VARCHAR(50) NOT NULL,
                    nomor_mesin VARCHAR(50) NOT NULL,
                    pajak DECIMAL(15,2) NOT NULL DEFAULT 0,
                    denda DECIMAL(15,2) NOT NULL DEFAULT 0,
                    tanggal_jatuh_tempo DATE NOT NULL,
                    tanggal_bayar DATE NULL,
                    status ENUM('BELUM_BAYAR', 'LUNAS', 'TERLAMBAT') DEFAULT 'BELUM_BAYAR',
                    path_gambar_stnk VARCHAR(500),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """;
            stmt.executeUpdate(createPajakTable);
            
            // Insert admin default jika belum ada
            String checkAdmin = "SELECT COUNT(*) FROM users WHERE role = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertAdmin = """
                    INSERT INTO users (username, password, nama_lengkap, role) 
                    VALUES ('admin', 'admin123', 'Administrator System', 'admin')
                """;
                stmt.executeUpdate(insertAdmin);
                System.out.println("Default admin created: username=admin, password=admin123");
            }
            
            System.out.println("Database schema created successfully");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error creating database schema: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}