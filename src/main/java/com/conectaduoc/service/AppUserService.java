package com.conectaduoc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conectaduoc.model.AppUser;
import com.conectaduoc.repository.AppUserRepository;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository usuarioRepository;

    public List<AppUser> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<AppUser> obtenerUsuarioPorId(Long idUser) {
        return usuarioRepository.findById(idUser);
    }

    public Optional<AppUser> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public AppUser guardarUsuario(AppUser usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long idUser) {
        usuarioRepository.deleteById(idUser);
    }

}
