package com.utsproyecto.ultimafila.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.EstadoBoleta;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {

	long countByFuncionIdAndEstadoNot(Long idFuncion, EstadoBoleta estado);

	@Query("SELECT b.silla.id FROM Boleta b WHERE b.funcion.id = :idFuncion AND b.estado <> :estado")
	List<Long> buscarIdsSillasOcupadas(@Param("idFuncion") Long idFuncion, @Param("estado") EstadoBoleta estado);
}
