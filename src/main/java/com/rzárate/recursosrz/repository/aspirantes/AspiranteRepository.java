package main.java.com.cisaacap.redchapina.repository.aspirantes;

import java.sql.*;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.rzárate.recursosrz.config.ConnectionDB;
import main.java.com.rzárate.recursosrz.dto.request.aspirantes.AspiranteRequest;
import main.java.com.rzárate.recursosrz.dto.response.aspirantes.AspiranteResponse;
import main.java.com.rzárate.recursosrz.exception.DataBaseException;

public class AspiranteRepository {

    public ObservableList<AspiranteResponse> findAll() {
        ObservableList<AspiranteResponse> list = FXCollections.observableArrayList();
        String sql = "SELECT a.id, a.nombre, a.apellido, a.correo, a.telefono, a.edad, d.direccion_linea "
                + "FROM aspirantes a JOIN direcciones d ON a.direccion_id = d.id";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new AspiranteResponse(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion_linea"),
                        rs.getInt("edad")
                ));
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error al consultar aspirantes", e);
        }
        return list;
    }

    public AspiranteResponse save(AspiranteRequest req) {
        String newUuid = UUID.randomUUID().toString();
        String sqlDir = "INSERT INTO direcciones (direccion_linea) VALUES (?)";
        String sqlAsp = "INSERT INTO aspirantes (id, nombre, apellido, correo, telefono, edad, direccion_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmDir = conn.prepareStatement(sqlDir, Statement.RETURN_GENERATED_KEYS)) {
                pstmDir.setString(1, req.getDireccion());
                pstmDir.executeUpdate();

                ResultSet rs = pstmDir.getGeneratedKeys();
                if (!rs.next()) {
                    throw new SQLException("Error al obtener ID de dirección");
                }
                int dirId = rs.getInt(1);

                try (PreparedStatement psAsp = conn.prepareStatement(sqlAsp)) {
                    psAsp.setString(1, newUuid);
                    psAsp.setString(2, req.getNombre());
                    psAsp.setString(3, req.getApellido());
                    psAsp.setString(4, req.getCorreo());
                    psAsp.setString(5, req.getTelefono());
                    psAsp.setInt(6, req.getEdad());
                    psAsp.setInt(7, dirId);
                    psAsp.executeUpdate();
                }

                conn.commit();
                return new AspiranteResponse(
                        newUuid,
                        req.getNombre(),
                        req.getApellido(),
                        req.getCorreo(),
                        req.getTelefono(),
                        req.getDireccion(),
                        req.getEdad()
                );
            } catch (SQLException ex) {
                conn.rollback();
                throw new DataBaseException("Error al guardar aspirante", ex);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error de conexión al guardar aspirante", e);
        }
    }

    public AspiranteResponse update(AspiranteRequest req) {
        String sqlAsp = "UPDATE aspirantes SET nombre=?, apellido=?, correo=?, telefono=?, edad=? WHERE id=?";
        String sqlDir = "UPDATE direcciones d JOIN aspirantes a ON d.id = a.direccion_id SET d.direccion_linea=? WHERE a.id=?";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psAsp = conn.prepareStatement(sqlAsp); PreparedStatement psDir = conn.prepareStatement(sqlDir)) {

                psAsp.setString(1, req.getNombre());
                psAsp.setString(2, req.getApellido());
                psAsp.setString(3, req.getCorreo());
                psAsp.setString(4, req.getTelefono());
                psAsp.setInt(5, req.getEdad());
                psAsp.setString(6, req.getIdAspirante());
                int rowsUpdated = psAsp.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    throw new DataBaseException("No se encontró el aspirante con ese ID.");
                }

                psDir.setString(1, req.getDireccion());
                psDir.setString(2, req.getIdAspirante());
                psDir.executeUpdate();

                conn.commit();
                return new AspiranteResponse(
                        req.getIdAspirante(),
                        req.getNombre(),
                        req.getApellido(),
                        req.getCorreo(),
                        req.getTelefono(),
                        req.getDireccion(),
                        req.getEdad()
                );
            } catch (SQLException ex) {
                conn.rollback();
                throw new DataBaseException("Error al actualizar aspirante", ex);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error de conexión al actualizar aspirante", e);
        }
    }

    public AspiranteResponse deleteById(String id) {
        String selectSql = "SELECT a.id, a.nombre, a.apellido, a.correo, a.telefono, a.edad, d.direccion_linea "
                + "FROM aspirantes a JOIN direcciones d ON a.direccion_id = d.id WHERE a.id = ?";
        String deleteSql = "DELETE FROM direcciones WHERE id = (SELECT direccion_id FROM aspirantes WHERE id = ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            AspiranteResponse deletedItem = null;

            try (PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
                psSelect.setString(1, id);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        deletedItem = new AspiranteResponse(
                                rs.getString("id"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("correo"),
                                rs.getString("telefono"),
                                rs.getString("direccion_linea"),
                                rs.getInt("edad")
                        );
                    } else {
                        throw new DataBaseException("No se encontró el aspirante a eliminar.");
                    }
                }
            }

            try (PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {
                psDelete.setString(1, id);
                psDelete.executeUpdate();
            }

            conn.commit();
            return deletedItem;

        } catch (SQLException e) {
            throw new DataBaseException("Error al eliminar aspirante", e);
        }
    }
}
