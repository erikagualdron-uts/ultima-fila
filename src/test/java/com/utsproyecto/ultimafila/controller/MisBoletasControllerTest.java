package com.utsproyecto.ultimafila.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MisBoletasControllerTest {

	// Scenario Outline de REQ-CIN-017: minutos para el inicio → resultado de la cancelación
	@ParameterizedTest
	@CsvSource({ "300, true", "120, true", "90, false", "0, false", "-30, false" })
	void plazoDeCancelacion(long minutosParaElInicio, boolean aceptada) {
		LocalDateTime ahora = LocalDateTime.of(2026, 12, 1, 12, 0);
		assertEquals(aceptada, MisBoletasController.dentroDelPlazo(ahora.plusMinutes(minutosParaElInicio), ahora));
	}
}
