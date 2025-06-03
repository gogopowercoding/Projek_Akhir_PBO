package basisdata;

import java.sql.*;
import javax.swing.JOptionPane;

public class KoneksiBasisData {
    private static final String URL = "jdbc:mysql://localhost:3306/pajak_kendaraan?useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "";

    // Mendapatkan koneksi ke database
    public static Connection getKoneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi database berhasil");
            return conn;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Driver MySQL tidak ditemukan: " + e.getMessage(),
                "Error Driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Gagal koneksi ke database: " + e.getMessage(),
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
