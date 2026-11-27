package com.utsproyecto.ultimafila.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.EstadoBoleta;

import jakarta.persistence.LockModeType;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {

	long countByFuncionIdAndEstadoNot(Long idFuncion, EstadoBoleta estado);

	boolean existsByCodigo(String codigo);

	@Query("SELECT b FROM Boleta b JOIN FETCH b.silla JOIN FETCH b.funcion f JOIN FETCH f.pelicula JOIN FETCH f.sala "
			+ "WHERE b.compra.id = :idCompra ORDER BY b.silla.fila, b.silla.numero")
	List<Boleta> buscarDeCompra(@Param("idCompra") Long idCompra);

	@Query("SELECT b FROM Boleta b JOIN FETCH b.compra c JOIN FETCH b.silla JOIN FETCH b.funcion f "
			+ "JOIN FETCH f.pelicula JOIN FETCH f.sala WHERE c.cliente.id = :idCliente "
			+ "ORDER BY c.fechaCompra DESC, c.id DESC, b.silla.fila, b.silla.numero")
	List<Boleta> buscarDeCliente(@Param("idCliente") Long idCliente);

	@Query("SELECT b FROM Boleta b JOIN FETCH b.compra c JOIN FETCH b.silla JOIN FETCH b.funcion f "
			+ "JOIN FETCH f.pelicula JOIN FETCH f.sala WHERE c.vendedor.id = :idVendedor "
			+ "AND c.fechaCompra >= :desde AND c.fechaCompra < :hasta "
			+ "ORDER BY c.fechaCompra DESC, c.id DESC, b.silla.fila, b.silla.numero")
	List<Boleta> buscarVentasDelDia(@Param("idVendedor") Long idVendedor, @Param("desde") LocalDateTime desde,
			@Param("hasta") LocalDateTime hasta);

	@Query("SELECT b.silla.id FROM Boleta b WHERE b.funcion.id = :idFuncion AND b.estado <> :estado")
	List<Long> buscarIdsSillasOcupadas(@Param("idFuncion") Long idFuncion, @Param("estado") EstadoBoleta estado);

	// Boletas no canceladas de una función con su silla, para el mapa de ocupación (REQ-CIN-021)
	@Query("SELECT b FROM Boleta b JOIN FETCH b.silla WHERE b.funcion.id = :idFuncion AND b.estado <> :estado")
	List<Boleta> buscarVigentesDeFuncion(@Param("idFuncion") Long idFuncion, @Param("estado") EstadoBoleta estado);

	// Ventas del día (REQ-CIN-022): boletas no canceladas de compras hechas entre dos instantes
	@Query("SELECT COUNT(b) FROM Boleta b WHERE b.estado <> :estado AND b.compra.fechaCompra >= :desde "
			+ "AND b.compra.fechaCompra < :hasta")
	long contarVendidasEntre(@Param("estado") EstadoBoleta estado, @Param("desde") LocalDateTime desde,
			@Param("hasta") LocalDateTime hasta);

	// Devuelve null cuando no hay ventas en el rango
	@Query("SELECT SUM(b.precio) FROM Boleta b WHERE b.estado <> :estado AND b.compra.fechaCompra >= :desde "
			+ "AND b.compra.fechaCompra < :hasta")
	BigDecimal sumarIngresoEntre(@Param("estado") EstadoBoleta estado, @Param("desde") LocalDateTime desde,
			@Param("hasta") LocalDateTime hasta);

	// Bloquea solo la fila de la boleta: dos acomodadores no pueden validarla a la vez (BR-CIN-006)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT b FROM Boleta b WHERE b.codigo = :codigo")
	Optional<Boleta> buscarParaValidar(@Param("codigo") String codigo);
}
