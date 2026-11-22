package com.utsproyecto.ultimafila.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

	@Query(value = "SELECT p FROM Pelicula p JOIN FETCH p.genero WHERE p.enCartelera = true ORDER BY p.titulo",
			countQuery = "SELECT COUNT(p) FROM Pelicula p WHERE p.enCartelera = true")
	Page<Pelicula> buscarEnCartelera(Pageable pageable);

	@Query("SELECT p FROM Pelicula p JOIN FETCH p.genero WHERE p.enCartelera = true AND EXISTS "
			+ "(SELECT f.id FROM Funcion f WHERE f.pelicula = p AND f.estado = :estado AND f.fechaHoraInicio > :ahora) "
			+ "ORDER BY p.titulo")
	List<Pelicula> buscarDestacadas(@Param("estado") EstadoFuncion estado, @Param("ahora") LocalDateTime ahora,
			Pageable pageable);
}
