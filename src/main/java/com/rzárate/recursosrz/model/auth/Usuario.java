package main.java.com.rzárate.recursosrz.model.auth;

public class Usuario {

    /* ===============================
            ATRIBUTOS
        =============================== */
    
    private String idUsuario;
    private String username;
    private String password;
    private int idRol;

    /* ===============================
            CONSTRUCTOR
        =============================== */
    
    public Usuario() {
    }

    public Usuario(String idUsuario, String username, String password, int idRol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.idRol = idRol;
    }

    /* ===============================
            GETTERS&SETTERS
        =============================== */
    
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}
