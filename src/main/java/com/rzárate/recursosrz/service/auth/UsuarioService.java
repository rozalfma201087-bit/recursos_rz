package main.java.com.rzárate.recursosrz.service.auth;

import main.java.com.rzárate.recursosrz.dto.request.auth.LoginRequest;
import main.java.com.rzárate.recursosrz.dto.request.auth.RegisterRequest;
import main.java.com.rzárate.recursosrz.dto.response.auth.RegisterResponse;
import main.java.com.rzárate.recursosrz.exception.ValidationException;
import main.java.com.rzárate.recursosrz.model.auth.Usuario;
import main.java.com.rzárate.recursosrz.repository.auth.UsuarioRepository;
import main.java.com.rzárate.recursosrz.security.jbcrypt.BCrypt;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public RegisterResponse registrarUsuario(RegisterRequest req) throws ValidationException {
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new ValidationException("El nombre de usuario es obligatorio.");
        }
        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new ValidationException("La contraseña debe tener al menos 6 caracteres.");
        }

        if (usuarioRepository.findByUsername(req.getUsername()) != null) {
            throw new ValidationException("El nombre de usuario ya está registrado.");
        }

        String hashedPassword = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());
        req.setPassword(hashedPassword);

        return usuarioRepository.save(req);
    }

    public Usuario login(LoginRequest req) throws ValidationException {
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new ValidationException("Ingrese su nombre de usuario.");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new ValidationException("Ingrese su contraseña.");
        }

        Usuario usuario = usuarioRepository.findByUsername(req.getUsername());

        if (usuario == null || !BCrypt.checkpw(req.getPassword(), usuario.getPassword())) {
            throw new ValidationException("Credenciales incorrectas.");
        }

        return usuario;
    }
}
