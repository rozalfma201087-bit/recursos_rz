package main.java.com.rzárate.recursosrz.repository.auth;

import java.sql.*;
import java.util.UUID;
import main.java.com.rzárate.recursosrz.config.ConnectionDB;
import main.java.com.rzárate.recursosrz.dto.request.auth.RegisterRequest;
import main.java.com.rzárate.recursosrz.dto.response.auth.RegisterResponse;
import main.java.com.rzárate.recursosrz.exception.DataBaseException;
import main.java.com.rzárate.recursosrz.model.auth.Usuario;

public class UsuarioRepository {

    public RegisterResponse save(RegisterRequest req) {
        String newUuid = UUID.randomUUID().toString();
        String sql = "INSERT INTO usuarios (id, username, password, rol_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {

                pstm.setString(1, newUuid);
                pstm.setString(2, req.getUsername());
                pstm.setString(3, req.getPassword());
                pstm.setInt(4, req.getIdRol());

                int rowsAffected = pstm.executeUpdate();
                conn.commit();

                if (rowsAffected > 0) {
                    return new RegisterResponse(newUuid, "Usuario registrado con éxito", true);
                } else {
                    return new RegisterResponse(null, "No se pudo registrar el usuario", false);
                }
            } catch (SQLException ex) {
                conn.rollback();
                throw new DataBaseException("Error al guardar usuario", ex);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error de conexión al guardar el usuario", e);
        }
    }

    public Usuario findByUsername(String username) {
        String sql = "SELECT id, username, password, rol_id FROM usuarios WHERE username = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, username);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("rol_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error al buscar el usuario por username" + e.getMessage());
        }
        return null;
    }
}