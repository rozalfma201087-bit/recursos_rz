package main.java.com.rzárate.recursosrz.dto.response.auth;

public class RegisterResponse {

    private String idUsuario;
    private String message;
    private boolean exito;

    public RegisterResponse(String idUsuario, String message, boolean exito) {
        this.idUsuario = idUsuario;
        this.message = message;
        this.exito = exito;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }
}
