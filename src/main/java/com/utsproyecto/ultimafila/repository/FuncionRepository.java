package com.utsproyecto.ultimafila.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;

public interface FuncionRepository extends JpaRepository<Funcion, Long> {

	@Query("SELECT f FROM Funcion f JOIN FETCH f.sala WHERE f.pelicula.id = :idPelicula AND f.estado = :estado "
			+ "AND f.fechaHoraInicio > :ahora ORDER BY f.fechaHoraInicio")
	List<Funcion> buscarFuturasDePelicula(@Param("idPelicula") Long idPelicula, @Param("estado") EstadoFuncion estado,
			@Param("ahora") LocalDateTime ahora);

	@Query("SELECT f FROM Funcion f JOIN FETCH f.pelicula JOIN FETCH f.sala WHERE f.estado = :estado "
			+ "AND f.fechaHoraInicio >= :desde AND f.fechaHoraInicio < :hasta ORDER BY f.fechaHoraInicio")
	List<Funcion> buscarPorDia(@Param("estado") EstadoFuncion estado, @Param("desde") LocalDateTime desde,
			@Param("hasta") LocalDateTime hasta);
}
