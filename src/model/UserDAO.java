package model;

import basisdata.KoneksiBasisData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.User;

public class UserDAO {
    
    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Gagal melakukan login");
        }
        return null;
    }
    
    public static boolean register(User user) {
        String sql = "INSERT INTO users (username, password, nama_lengkap, alamat, no_telp, email, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNamaLengkap());
            ps.setString(4, user.getAlamat());
            ps.setString(5, user.getNoTelp());
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getRole());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                JOptionPane.showMessageDialog(null, 
                    "Username sudah terdaftar, silakan gunakan username lain", 
                    "Registration Error", JOptionPane.WARNING_MESSAGE);
            } else {
                handleSQLException(e, "Gagal melakukan registrasi");
            }
        }
        return false;
    }
    
    public static boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Gagal memeriksa username");
        }
        return false;
    }
    
    public static List<User> getAllPembayar() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'pembayar' ORDER BY nama_lengkap";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e, "Gagal mengambil data pembayar pajak");
        }
        return list;
    }
    
    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET nama_lengkap=?, alamat=?, no_telp=?, email=? WHERE id=?";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, user.getNamaLengkap());
            ps.setString(2, user.getAlamat());
            ps.setString(3, user.getNoTelp());
            ps.setString(4, user.getEmail());
            ps.setInt(5, user.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Gagal mengupdate data user");
        }
        return false;
    }
    
    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User(
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("nama_lengkap"),
            rs.getString("alamat"),
            rs.getString("no_telp"),
            rs.getString("email"),
            rs.getString("role")
        );
        user.setId(rs.getInt("id"));
        return user;
    }
    
    private static void handleSQLException(SQLException e, String customMessage) {
        String errorMessage = customMessage + "\n" +
                            "Error Code: " + e.getErrorCode() + "\n" +
                            "SQL State: " + e.getSQLState() + "\n" +
                            "Message: " + e.getMessage();
        
        System.err.println(errorMessage);
        JOptionPane.showMessageDialog(null, 
            errorMessage, 
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}