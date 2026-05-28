package com.SanosySalvos.IdentificacioAcceso.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "usuario-service", url = "${usuario.service.url}")
public interface UsuarioClient {

    @PostMapping(value = "/api/usuarios/interno/crear-perfil", consumes = "text/plain")
    void crearPerfilInicial(@RequestBody String correo);
}