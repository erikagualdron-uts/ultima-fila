package com.utsproyecto.ultimafila.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.model.Rol;
import com.utsproyecto.ultimafila.model.Sala;
import com.utsproyecto.ultimafila.model.Silla;
import com.utsproyecto.ultimafila.repository.BoletaRepository;

class CompraControllerTest {

	// Scenario Outline de REQ-CIN-015: minutos para el inicio, boletas, ya vendidas → resultado
	@ParameterizedTest
	@CsvSource(delimiter = ';', value = { "180;2;0;", "180;6;0;", "180;7;0;Máximo 6 boletas por compra",
			"180;2;1;La silla A1 ya fue vendida", "-6;2;0;La función ya inició" })
	void reglasDeCompra(long minutosParaElInicio, int boletas, int vendidas, String esperado) {
		Funcion funcion = new Funcion();
		funcion.setId(1L);
		funcion.setEstado(EstadoFuncion.PROGRAMADA);
		funcion.setPrecio(new BigDecimal("12000"));
		funcion.setFechaHoraInicio(LocalDateTime.now().plusMinutes(minutosParaElInicio));
		Sala sala = new Sala();
		sala.setId(1L);
		funcion.setSala(sala);

		List<Silla> sillas = IntStream.rangeClosed(1, boletas).mapToObj(n -> {
			Silla silla = new Silla();
			silla.setId((long) n);
			silla.setFila("A");
			silla.setNumero(n);
			silla.setActiva(true);
			silla.setSala(sala);
			return silla;
		}).toList();

		BoletaRepository boletaRepository = mock(BoletaRepository.class);
		List<Long> idsVendidos = IntStream.rangeClosed(1, vendidas).mapToObj(Long::valueOf).toList();
		when(boletaRepository.buscarIdsSillasOcupadas(eq(1L), any(EstadoBoleta.class))).thenReturn(idsVendidos);
		CompraController controlador = new CompraController(null, null, null, boletaRepository, null);

		String motivo = controlador.motivoRechazo(funcion, sillas, Rol.CLIENTE);
		if (esperado == null || esperado.isEmpty()) {
			assertNull(motivo);
		} else {
			assertEquals(esperado, motivo);
		}
	}
}
