package com.utsproyecto.ultimafila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utsproyecto.ultimafila.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
}
