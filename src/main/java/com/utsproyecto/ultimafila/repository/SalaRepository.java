package com.utsproyecto.ultimafila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utsproyecto.ultimafila.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {

	List<Sala> findByActivaTrueOrderByNombre();

	List<Sala> findAllByOrderByNombre();

	boolean existsByNombreIgnoreCase(String nombre);
}
