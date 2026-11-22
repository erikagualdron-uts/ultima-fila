package com.utsproyecto.ultimafila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utsproyecto.ultimafila.model.Silla;

public interface SillaRepository extends JpaRepository<Silla, Long> {

	long countBySalaIdAndActivaTrue(Long idSala);

	List<Silla> findBySalaIdOrderByFilaAscNumeroAsc(Long idSala);
}
