package com.utsproyecto.ultimafila.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utsproyecto.ultimafila.model.Rol;
import com.utsproyecto.ultimafila.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByCorreo(String correo);

	Optional<Usuario> findByCorreo(String correo);

	List<Usuario> findAllByOrderByRolAscNombreCompletoAsc();

	List<Usuario> findByRolOrderByNombreCompleto(Rol rol);
}
