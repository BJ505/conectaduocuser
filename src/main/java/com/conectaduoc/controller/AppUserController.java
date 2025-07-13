package com.conectaduoc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conectaduoc.exception.ResourceNotFoundException;
import com.conectaduoc.model.AppUser;
import com.conectaduoc.service.AppUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@Validated
public class AppUserController {

    @Autowired
    private AppUserService usuarioService;

    @GetMapping
    public ResponseEntity<List<AppUser>> listasUsuarios() {
        List<AppUser> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Crear un nuevo usuario con validación
    @PostMapping
    public ResponseEntity<AppUser> crearUsuario(@Valid @RequestBody AppUser usuario) {
        AppUser nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED); // Devolver 201 Created
    }

    // Obtener un usuario por su ID con manejo de excepción
    @GetMapping("/{idUser}")
    public ResponseEntity<AppUser> obtenerUsuarioPorId(@PathVariable Long idUser) {
        AppUser usuario = usuarioService.obtenerUsuarioPorId(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + idUser));
        return ResponseEntity.ok(usuario);
    }

    // Eliminar un usuario por su email con manejo de excepción
    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long idUser) {
        // Verifica si el usuario existe, si no, lanza la excepción
        usuarioService.obtenerUsuarioPorId(idUser)
                .orElseThrow(
                        () -> new ResourceNotFoundException("El usuario con id " + idUser + " no fue encontrado."));

        // Elimina el usuario si existe
        usuarioService.eliminarUsuario(idUser);

        return ResponseEntity.noContent().build(); // Devolver 204 No Content
    }

    // Actualizar un usuario existente con manejo de excepción
    @PutMapping("/{idUsuario}")
    public ResponseEntity<AppUser> actualizarUsuario(@PathVariable Long idUsuario,
            @Valid @RequestBody AppUser detallesUsuario) {
        AppUser usuario = usuarioService.obtenerUsuarioPorId(idUsuario)
                .orElseThrow(
                        () -> new ResourceNotFoundException("El usuario con id " + idUsuario + " no fue encontrado."));

        usuario.setName(detallesUsuario.getName());
        usuario.setRole(detallesUsuario.getRole());
        usuario.setCenter(detallesUsuario.getCenter());
        AppUser usuarioActualizado = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /* Endpoints Adicionales */
    @GetMapping("/exists/{email}")
    public ResponseEntity<Map<String, Object>> checkUserExists(@PathVariable String email) {
        return usuarioService.obtenerUsuarioPorEmail(email)
            .map(usuario -> {
                Map<String, Object> response = new HashMap<>();
                response.put("exists", true);
                response.put("idUser", usuario.getIdUser());
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("exists", false);
                response.put("idUser", null);
                return ResponseEntity.ok(response);
        });
}

    // Obtener un usuario por su ID (numérico) con manejo de excepción
    @GetMapping("/id/{idUser}")
    public ResponseEntity<AppUser> obtenerUsuarioPorIdNumerico(@PathVariable Long idUser) {
        AppUser usuario = usuarioService.obtenerUsuarioPorId(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + idUser));
        return ResponseEntity.ok(usuario);
    }

}
