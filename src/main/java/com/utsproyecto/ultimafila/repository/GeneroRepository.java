package com.utsproyecto.ultimafila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utsproyecto.ultimafila.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {

	List<Genero> findAllByOrderByNombre();

	@Query("SELECT COUNT(g) > 0 FROM Genero g WHERE LOWER(g.nombre) = LOWER(:nombre) AND g.id <> :id")
	boolean existeNombre(@Param("nombre") String nombre, @Param("id") long id);

	@Query("SELECT COUNT(p) FROM Pelicula p WHERE p.genero.id = :id")
	long contarPeliculas(@Param("id") Long id);
}
