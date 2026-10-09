package main.java.com.rzárate.recursosrz.service.aspirantes;

import javafx.collections.ObservableList;
import main.java.com.cisaacap.redchapina.repository.aspirantes.AspiranteRepository;
import main.java.com.rzárate.recursosrz.dto.request.aspirantes.AspiranteRequest;
import main.java.com.rzárate.recursosrz.dto.response.aspirantes.AspiranteResponse;
import main.java.com.rzárate.recursosrz.exception.ValidationException;

public class AspiranteService {

    private final AspiranteRepository repository;

    public AspiranteService(AspiranteRepository repository) {
        this.repository = repository;
    }

    public ObservableList<AspiranteResponse> listarAspirantes() {
        return repository.findAll();
    }

    public AspiranteResponse registrarAspirante(AspiranteRequest req) throws ValidationException {
        validarCampos(req);
        return repository.save(req);
    }

    public AspiranteResponse actualizarAspirante(AspiranteRequest req) throws ValidationException {
        if (req.getIdAspirante() == null || req.getIdAspirante().isBlank()) {
            throw new ValidationException("El ID del aspirante es requerido para actualizar.");
        }
        validarCampos(req);
        return repository.update(req);
    }

    public AspiranteResponse eliminarAspirante(String id) throws ValidationException {
        if (id == null || id.isBlank()) {
            throw new ValidationException("Seleccione un registro válido para eliminar.");
        }
        return repository.deleteById(id);
    }

    private void validarCampos(AspiranteRequest req) throws ValidationException {
        if (req.getNombre() == null || req.getNombre().isBlank()) {
            throw new ValidationException("El nombre es requerido.");
        }
        if (req.getCorreo() == null || !req.getCorreo().contains("@")) {
            throw new ValidationException("El correo ingresado no es válido.");
        }
        if (req.getEdad() < 18) {
            throw new ValidationException("El aspirante debe ser mayor o igual a 18 años.");
        }
    }
}
