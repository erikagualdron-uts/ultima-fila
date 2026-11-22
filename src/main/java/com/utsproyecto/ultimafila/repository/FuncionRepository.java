package com.utsproyecto.ultimafila.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;

import jakarta.persistence.LockModeType;

public interface FuncionRepository extends JpaRepository<Funcion, Long> {

	@Query("SELECT f FROM Funcion f JOIN FETCH f.pelicula JOIN FETCH f.sala WHERE f.id = :id")
	Optional<Funcion> buscarConDetalle(@Param("id") Long id);

	// Bloquea la fila de la función: dos compras simultáneas de la misma función se atienden una detrás de otra
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT f FROM Funcion f WHERE f.id = :id")
	Optional<Funcion> buscarConBloqueo(@Param("id") Long id);

	@Query("SELECT f FROM Funcion f JOIN FETCH f.sala WHERE f.pelicula.id = :idPelicula AND f.estado = :estado "
			+ "AND f.fechaHoraInicio > :ahora ORDER BY f.fechaHoraInicio")
	List<Funcion> buscarFuturasDePelicula(@Param("idPelicula") Long idPelicula, @Param("estado") EstadoFuncion estado,
			@Param("ahora") LocalDateTime ahora);

	@Query("SELECT f FROM Funcion f JOIN FETCH f.pelicula JOIN FETCH f.sala WHERE f.estado = :estado "
			+ "AND f.fechaHoraInicio >= :desde AND f.fechaHoraInicio < :hasta ORDER BY f.fechaHoraInicio")
	List<Funcion> buscarPorDia(@Param("estado") EstadoFuncion estado, @Param("desde") LocalDateTime desde,
			@Param("hasta") LocalDateTime hasta);
}
