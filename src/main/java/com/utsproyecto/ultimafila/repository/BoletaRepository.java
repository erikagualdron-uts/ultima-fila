package com.utsproyecto.ultimafila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.EstadoBoleta;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {

	long countByFuncionIdAndEstadoNot(Long idFuncion, EstadoBoleta estado);
}
