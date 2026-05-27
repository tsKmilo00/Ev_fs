package com.SanosySalvos.IdentificacioAcceso.service;

import com.SanosySalvos.IdentificacioAcceso.dto.LoginRequest;
import com.SanosySalvos.IdentificacioAcceso.dto.RegistroRequest;
import com.SanosySalvos.IdentificacioAcceso.model.UsuarioModel;
import com.SanosySalvos.IdentificacioAcceso.repository.UsuarioRepository;
import com.SanosySalvos.IdentificacioAcceso.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void registrarUsuario(RegistroRequest peticion) {
        Optional<UsuarioModel> existeUsuario = usuarioRepository.findByCorreo(peticion.getCorreo());
        if (existeUsuario.isPresent()) {
            throw new RuntimeException("Este correo ya está en uso. ¿Deseas iniciar sesión?");
        }

        UsuarioModel nuevoUsuario = new UsuarioModel(
                peticion.getCorreo(),
                passwordEncoder.encode(peticion.getContrasena()),
                peticion.getRol(),
                peticion.getNombreInstitucion(),
                peticion.getUrlLogoInstitucion()
        );

        usuarioRepository.save(nuevoUsuario);
    }

    public String autenticarUsuario(LoginRequest peticion) {
        UsuarioModel usuario = usuarioRepository.findByCorreo(peticion.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(peticion.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return jwtUtil.generateToken(usuario.getCorreo());
    }
}
