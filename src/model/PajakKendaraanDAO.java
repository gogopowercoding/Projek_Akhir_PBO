package model;

import basisdata.KoneksiBasisData;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PajakKendaraanDAO {
    
    public static boolean tambah(PajakKendaraan pajak) {
        String sql = """
            INSERT INTO pajak_kendaraan 
            (user_id, nomor_polisi, jenis_kendaraan, merk, type, tahun_kendaraan, 
             nomor_stnk, nomor_rangka, nomor_mesin, pajak, denda, tanggal_jatuh_tempo, 
             status, path_gambar_stnk) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, pajak.getUserId());
            ps.setString(2, pajak.getNomorPolisi());
            ps.setString(3, pajak.getJenisKendaraan());
            ps.setString(4, pajak.getMerk());
            ps.setString(5, pajak.getType());
            ps.setInt(6, pajak.getTahunKendaraan());
            ps.setString(7, pajak.getNomorSTNK());
            ps.setString(8, pajak.getNomorRangka());
            ps.setString(9, pajak.getNomorMesin());
            ps.setDouble(10, pajak.getPajak());
            ps.setDouble(11, pajak.getDenda());
            ps.setDate(12, Date.valueOf(pajak.getTanggalJatuhTempo()));
            ps.setString(13, pajak.getStatus());
            ps.setString(14, pajak.getPathGambarSTNK());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pajak.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            handleSQLException(e, "Gagal menambah data pajak kendaraan");
        }
        return false;
    }
    
    public static boolean update(PajakKendaraan pajak) {
        String sql = """
            UPDATE pajak_kendaraan SET 
            nomor_polisi=?, jenis_kendaraan=?, merk=?, type=?, tahun_kendaraan=?, 
            nomor_stnk=?, nomor_rangka=?, nomor_mesin=?, pajak=?, denda=?, 
            tanggal_jatuh_tempo=?, status=?, path_gambar_stnk=?
            WHERE id=?
        """;
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, pajak.getNomorPolisi());
            ps.setString(2, pajak.getJenisKendaraan());
            ps.setString(3, pajak.getMerk());
            ps.setString(4, pajak.getType());
            ps.setInt(5, pajak.getTahunKendaraan());
            ps.setString(6, pajak.getNomorSTNK());
            ps.setString(7, pajak.getNomorRangka());
            ps.setString(8, pajak.getNomorMesin());
            ps.setDouble(9, pajak.getPajak());
            ps.setDouble(10, pajak.getDenda());
            ps.setDate(11, Date.valueOf(pajak.getTanggalJatuhTempo()));
            ps.setString(12, pajak.getStatus());
            ps.setString(13, pajak.getPathGambarSTNK());
            ps.setInt(14, pajak.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Gagal mengupdate data pajak kendaraan");
        }
        return false;
    }
    
    public static boolean bayar(int id) {
        String sql = "UPDATE pajak_kendaraan SET status='LUNAS', tanggal_bayar=CURRENT_DATE WHERE id=?";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Gagal memproses pembayaran");
        }
        return false;
    }
    
    public static boolean hapus(int id) {
        String sql = "DELETE FROM pajak_kendaraan WHERE id=?";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Gagal menghapus data pajak kendaraan");
        }
        return false;
    }
    
    public static List<PajakKendaraan> getAllByUserId(int userId) {
        List<PajakKendaraan> list = new ArrayList<>();
        String sql = "SELECT * FROM pajak_kendaraan WHERE user_id = ? ORDER BY tanggal_jatuh_tempo DESC";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPajakKendaraan(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Gagal mengambil data pajak kendaraan");
        }
        return list;
    }
    
    public static List<PajakKendaraan> getAll() {
        List<PajakKendaraan> list = new ArrayList<>();
        String sql = """
            SELECT pk.*, u.nama_lengkap as nama_pemilik 
            FROM pajak_kendaraan pk 
            JOIN users u ON pk.user_id = u.id 
            ORDER BY pk.tanggal_jatuh_tempo DESC
        """;
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                PajakKendaraan pajak = mapResultSetToPajakKendaraan(rs);
                list.add(pajak);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Gagal mengambil semua data pajak kendaraan");
        }
        return list;
    }
    
    public static List<PajakKendaraan> getBelumBayar() {
        List<PajakKendaraan> list = new ArrayList<>();
        String sql = """
            SELECT pk.*, u.nama_lengkap as nama_pemilik 
            FROM pajak_kendaraan pk 
            JOIN users u ON pk.user_id = u.id 
            WHERE pk.status IN ('BELUM_BAYAR', 'TERLAMBAT')
            ORDER BY pk.tanggal_jatuh_tempo ASC
        """;
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToPajakKendaraan(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e, "Gagal mengambil data pajak yang belum dibayar");
        }
        return list;
    }
    
    public static void updateStatusTerlambat() {
        String sql = "UPDATE pajak_kendaraan SET status='TERLAMBAT' WHERE tanggal_jatuh_tempo < CURRENT_DATE AND status='BELUM_BAYAR'";
        
        try (Connection c = KoneksiBasisData.getKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e, "Gagal mengupdate status terlambat");
        }
    }
    
    private static PajakKendaraan mapResultSetToPajakKendaraan(ResultSet rs) throws SQLException {
        PajakKendaraan pajak = new PajakKendaraan();
        pajak.setId(rs.getInt("id"));
        pajak.setUserId(rs.getInt("user_id"));
        pajak.setNomorPolisi(rs.getString("nomor_polisi"));
        pajak.setJenisKendaraan(rs.getString("jenis_kendaraan"));
        pajak.setMerk(rs.getString("merk"));
        pajak.setType(rs.getString("type"));
        pajak.setTahunKendaraan(rs.getInt("tahun_kendaraan"));
        pajak.setNomorSTNK(rs.getString("nomor_stnk"));
        pajak.setNomorRangka(rs.getString("nomor_rangka"));
        pajak.setNomorMesin(rs.getString("nomor_mesin"));
        pajak.setPajak(rs.getDouble("pajak"));
        pajak.setDenda(rs.getDouble("denda"));
        
        Date tanggalJatuhTempo = rs.getDate("tanggal_jatuh_tempo");
        if (tanggalJatuhTempo != null) {
            pajak.setTanggalJatuhTempo(tanggalJatuhTempo.toLocalDate());
        }
        
        Date tanggalBayar = rs.getDate("tanggal_bayar");
        if (tanggalBayar != null) {
            pajak.setTanggalBayar(tanggalBayar.toLocalDate());
        }
        
        pajak.setStatus(rs.getString("status"));
        pajak.setPathGambarSTNK(rs.getString("path_gambar_stnk"));
        
        return pajak;
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