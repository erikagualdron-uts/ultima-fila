package com.utsproyecto.ultimafila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utsproyecto.ultimafila.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
