package com.utsproyecto.ultimafila.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utsproyecto.ultimafila.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

	boolean existsByCodigo(String codigo);

	@Query("SELECT c FROM Compra c LEFT JOIN FETCH c.cliente LEFT JOIN FETCH c.vendedor WHERE c.codigo = :codigo")
	Optional<Compra> buscarPorCodigo(@Param("codigo") String codigo);
}
