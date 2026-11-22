package com.utsproyecto.ultimafila.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.utsproyecto.ultimafila.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

	@Query(value = "SELECT p FROM Pelicula p JOIN FETCH p.genero WHERE p.enCartelera = true ORDER BY p.titulo",
			countQuery = "SELECT COUNT(p) FROM Pelicula p WHERE p.enCartelera = true")
	Page<Pelicula> buscarEnCartelera(Pageable pageable);
}
