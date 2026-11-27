package com.utsproyecto.ultimafila.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.Funcion;

class AcomodadorControllerTest {

	private static final LocalDateTime INICIO = LocalDateTime.of(2026, 12, 1, 18, 0);

	private static Boleta boleta(EstadoBoleta estado) {
		Funcion funcion = new Funcion();
		funcion.setFechaHoraInicio(INICIO);
		funcion.setFechaHoraFin(INICIO.plusMinutes(135));
		Boleta boleta = new Boleta();
		boleta.setFuncion(funcion);
		boleta.setEstado(estado);
		return boleta;
	}

	// BR-CIN-006: ventana de ingreso, desde 30 min antes del inicio hasta el fin de la función (límites incluidos)
	@ParameterizedTest
	@CsvSource(delimiter = ';', value = { "-120;Aún no se permite el ingreso", "-31;Aún no se permite el ingreso",
			"-30;", "-15;", "0;", "135;", "136;La función ya terminó" })
	void ventanaDeIngreso(long minutosDesdeElInicio, String esperado) {
		String motivo = AcomodadorController.motivoRechazoIngreso(boleta(EstadoBoleta.VENDIDA),
				INICIO.plusMinutes(minutosDesdeElInicio));
		if (esperado == null || esperado.isEmpty()) {
			assertNull(motivo);
		} else {
			assertEquals(esperado, motivo);
		}
	}

	// AC-020.2: una boleta cancelada se rechaza sin importar la hora
	@Test
	void boletaCancelada() {
		assertEquals("Boleta cancelada",
				AcomodadorController.motivoRechazoIngreso(boleta(EstadoBoleta.CANCELADA), INICIO.minusMinutes(15)));
	}

	// AC-019.2: una boleta ya validada muestra la hora de su ingreso
	@Test
	void boletaYaUsada() {
		Boleta usada = boleta(EstadoBoleta.USADA);
		usada.setFechaIngreso(INICIO.minusMinutes(15));
		assertEquals("Boleta ya usada a las 17:45",
				AcomodadorController.motivoRechazoIngreso(usada, INICIO.minusMinutes(10)));
	}
}
