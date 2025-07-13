package com.conectaduoc.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    @Test
    void testGettersAndSetters() {
        AppUser user = new AppUser();
        user.setIdUser(1L);
        user.setName("Nombre");
        user.setEmail("correo@dominio.com");
        user.setRole("ADMIN");
        user.setPolicies(1);
        user.setCenter("Centro de Prueba");

        assertEquals(1L, user.getIdUser());
        assertEquals("Nombre", user.getName());
        assertEquals("correo@dominio.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertEquals(1, user.getPolicies());
        assertEquals("Centro de Prueba", user.getCenter());
    }
}
