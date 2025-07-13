package com.conectaduoc.service;

import com.conectaduoc.model.AppUser;
import com.conectaduoc.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository repository;

    @InjectMocks
    private AppUserService service;

    @Test
    void listarUsuarios() {
        List<AppUser> usuarios = new ArrayList<>();
        AppUser user1 = new AppUser();
        user1.setIdUser(1L);
        user1.setEmail("user1@example.com");
        usuarios.add(user1);
    }

    @Test
    void obtenerUsuarioPorId() {
        AppUser user = new AppUser();
        user.setIdUser(1L);
        user.setEmail("user1@example.com");

        // Aquí, el mock responde "cuando busquen el id 1, entrega el usuario"
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        Optional<AppUser> result = service.obtenerUsuarioPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("user1@example.com", result.get().getEmail());
        verify(repository, times(1)).findById(1L);
    }


    @Test
    void obtenerUsuarioPorEmail() {
        AppUser user = new AppUser();
        user.setIdUser(1L);
        user.setEmail("user1@example.com");

        repository.save(user);
        when(repository.findByEmail("user1@example.com")).thenReturn(Optional.of(user));

        Optional<AppUser> result = service.obtenerUsuarioPorEmail("user1@example.com");
        assertTrue(result.isPresent());
        assertEquals("user1@example.com", result.get().getEmail());
        verify(repository, times(1)).findByEmail("user1@example.com");
    }   

    @Test
    void guardarUsuario() {
        AppUser user = new AppUser();
        user.setIdUser(1L);
        user.setEmail("user1@example.com");
        // Configura el mock ANTES de usarlo
        when(repository.save(user)).thenReturn(user);

        AppUser result = service.guardarUsuario(user);
        assertEquals("user1@example.com", result.getEmail());
        verify(repository, times(1)).save(user);
    }


    @Test
    void eliminarUsuario() {
        Long userId = 1L;

        doNothing().when(repository).deleteById(userId);

        service.eliminarUsuario(userId);
        verify(repository, times(1)).deleteById(userId);
    }
}
