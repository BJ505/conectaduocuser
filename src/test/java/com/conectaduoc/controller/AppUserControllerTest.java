package com.conectaduoc.controller;

import com.conectaduoc.model.AppUser;
import com.conectaduoc.service.AppUserService;
import com.conectaduoc.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppUserControllerTest {

    @Mock
    private AppUserService userService;

    @InjectMocks
    private AppUserController controller;

    @Test
    void listUsers_debeRetornarLista() {
        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();
        List<AppUser> users = Arrays.asList(user1, user2);

        when(userService.listarUsuarios()).thenReturn(users);

        ResponseEntity<List<AppUser>> response = controller.listasUsuarios();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void createUser_debeRetornarUsuarioCreado() {
        AppUser newUser = new AppUser();
        when(userService.guardarUsuario(newUser)).thenReturn(newUser);

        ResponseEntity<AppUser> response = controller.crearUsuario(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser, response.getBody());
    }

    @Test
    void getUserById_debeRetornarUsuario() {
        Long userId = 1L;
        AppUser user = new AppUser();
        when(userService.obtenerUsuarioPorId(userId)).thenReturn(Optional.of(user));

        ResponseEntity<AppUser> response = controller.obtenerUsuarioPorId(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserById_debeLanzarExcepcionSiNoExiste() {
        Long userId = 1L;
        when(userService.obtenerUsuarioPorId(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            controller.obtenerUsuarioPorId(userId);
        });

        assertEquals("Usuario no encontrado con id: " + userId, exception.getMessage());
    }

    @Test
    void deleteUser_debeEliminarUsuario() {
        Long userId = 1L;
        AppUser user = new AppUser();
        when(userService.obtenerUsuarioPorId(userId)).thenReturn(Optional.of(user));

        ResponseEntity<Void> response = controller.eliminarUsuario(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(userService).eliminarUsuario(userId);
    }

    @Test
    void deleteUser_debeLanzarExcepcionSiNoExiste() {
        Long userId = 1L;
        when(userService.obtenerUsuarioPorId(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            controller.eliminarUsuario(userId);
        });

        assertEquals("El usuario con id " + userId + " no fue encontrado.", exception.getMessage());
    }

    @Test
void updateUser_debeActualizarUsuario() {
    Long userId = 1L;
    AppUser existingUser = new AppUser();
    existingUser.setIdUser(userId);
    AppUser updatedUser = new AppUser();
    updatedUser.setIdUser(userId);
    updatedUser.setName("Updated Name");

    when(userService.obtenerUsuarioPorId(userId)).thenReturn(Optional.of(existingUser));
    when(userService.guardarUsuario(any(AppUser.class))).thenReturn(updatedUser); // <-- CAMBIO AQUÍ

    ResponseEntity<AppUser> response = controller.actualizarUsuario(userId, updatedUser);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedUser, response.getBody());

    verify(userService).guardarUsuario(any(AppUser.class));
}

    @Test
    void  getUserByEmail_debeRetornarUsuario() {
        String email = "test@example.com";
        AppUser user = new AppUser();
        user.setEmail(email);
        when(userService.obtenerUsuarioPorEmail(email)).thenReturn(Optional.of(user));

        Optional<AppUser> response = userService.obtenerUsuarioPorEmail(email);
        assertTrue(response.isPresent());
        assertEquals(user, response.get());
    }

    @Test
    void getUserByEmail_debeRetornarVacioSiNoExiste() {
        String email = "test@example.com";
        when(userService.obtenerUsuarioPorEmail(email)).thenReturn(Optional.empty());

        Optional<AppUser> response = userService.obtenerUsuarioPorEmail(email);
        assertFalse(response.isPresent());
    }

    @Test
    void getUserByNumericId_debeRetornarUsuario() {
        Long userId = 1L;
        AppUser user = new AppUser();
        user.setIdUser(userId);
        when(userService.obtenerUsuarioPorId(userId)).thenReturn(Optional.of(user));

        ResponseEntity<AppUser> response = controller.obtenerUsuarioPorIdNumerico(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
}
