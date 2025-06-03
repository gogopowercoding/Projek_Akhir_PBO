package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String namaLengkap;
    private String alamat;
    private String noTelp;
    private String email;
    private String role;

    // Konstruktor yang sesuai dengan parameter di RegisterFrame
    public User(String username, String password, String namaLengkap, String alamat, String noTelp, String email, String role) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.email = email;
        this.role = role;
    }

    // Getter dan Setter...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
