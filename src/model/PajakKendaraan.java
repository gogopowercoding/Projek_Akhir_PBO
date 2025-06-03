package model;

import java.time.LocalDate;

public class PajakKendaraan {
    private int id;
    private int userId;
    private String nomorPolisi;
    private String jenisKendaraan;
    private String merk;
    private String type;
    private int tahunKendaraan;
    private String nomorSTNK;
    private String nomorRangka;
    private String nomorMesin;
    private double pajak;
    private double denda;
    private double totalBayar;
    private LocalDate tanggalJatuhTempo;
    private LocalDate tanggalBayar;
    private String status; // "BELUM_BAYAR", "LUNAS", "TERLAMBAT"
    private String pathGambarSTNK;
    
    public PajakKendaraan() {}
    
    public PajakKendaraan(int id, int userId, String nomorPolisi, String jenisKendaraan, 
                         String merk, String type, int tahunKendaraan, String nomorSTNK,
                         String nomorRangka, String nomorMesin, double pajak, double denda,
                         LocalDate tanggalJatuhTempo, String status, String pathGambarSTNK) {
        this.id = id;
        this.userId = userId;
        this.nomorPolisi = nomorPolisi;
        this.jenisKendaraan = jenisKendaraan;
        this.merk = merk;
        this.type = type;
        this.tahunKendaraan = tahunKendaraan;
        this.nomorSTNK = nomorSTNK;
        this.nomorRangka = nomorRangka;
        this.nomorMesin = nomorMesin;
        this.pajak = pajak;
        this.denda = denda;
        this.totalBayar = pajak + denda;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.status = status;
        this.pathGambarSTNK = pathGambarSTNK;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getNomorPolisi() { return nomorPolisi; }
    public void setNomorPolisi(String nomorPolisi) { this.nomorPolisi = nomorPolisi; }
    
    public String getJenisKendaraan() { return jenisKendaraan; }
    public void setJenisKendaraan(String jenisKendaraan) { this.jenisKendaraan = jenisKendaraan; }
    
    public String getMerk() { return merk; }
    public void setMerk(String merk) { this.merk = merk; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getTahunKendaraan() { return tahunKendaraan; }
    public void setTahunKendaraan(int tahunKendaraan) { this.tahunKendaraan = tahunKendaraan; }
    
    public String getNomorSTNK() { return nomorSTNK; }
    public void setNomorSTNK(String nomorSTNK) { this.nomorSTNK = nomorSTNK; }
    
    public String getNomorRangka() { return nomorRangka; }
    public void setNomorRangka(String nomorRangka) { this.nomorRangka = nomorRangka; }
    
    public String getNomorMesin() { return nomorMesin; }
    public void setNomorMesin(String nomorMesin) { this.nomorMesin = nomorMesin; }
    
    public double getPajak() { return pajak; }
    public void setPajak(double pajak) { 
        this.pajak = pajak;
        this.totalBayar = this.pajak + this.denda;
    }
    
    public double getDenda() { return denda; }
    public void setDenda(double denda) { 
        this.denda = denda;
        this.totalBayar = this.pajak + this.denda;
    }
    
    public double getTotalBayar() { return totalBayar; }
    
    public LocalDate getTanggalJatuhTempo() { return tanggalJatuhTempo; }
    public void setTanggalJatuhTempo(LocalDate tanggalJatuhTempo) { this.tanggalJatuhTempo = tanggalJatuhTempo; }
    
    public LocalDate getTanggalBayar() { return tanggalBayar; }
    public void setTanggalBayar(LocalDate tanggalBayar) { this.tanggalBayar = tanggalBayar; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPathGambarSTNK() { return pathGambarSTNK; }
    public void setPathGambarSTNK(String pathGambarSTNK) { this.pathGambarSTNK = pathGambarSTNK; }
}