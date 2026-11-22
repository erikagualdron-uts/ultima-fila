package com.utsproyecto.ultimafila.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.model.Silla;
import com.utsproyecto.ultimafila.repository.BoletaRepository;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;

@Controller
public class SillasController {

	private final FuncionRepository funcionRepository;
	private final SillaRepository sillaRepository;
	private final BoletaRepository boletaRepository;

	public SillasController(FuncionRepository funcionRepository, SillaRepository sillaRepository,
			BoletaRepository boletaRepository) {
		this.funcionRepository = funcionRepository;
		this.sillaRepository = sillaRepository;
		this.boletaRepository = boletaRepository;
	}

	@GetMapping("/funciones/{id}/sillas")
	public String mapa(@PathVariable Long id, Model model) {
		Funcion funcion = funcionRepository.buscarConDetalle(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Silla> sillas = sillaRepository.findBySalaIdOrderByFilaAscNumeroAsc(funcion.getSala().getId());
		Map<String, List<Silla>> filas = sillas.stream()
				.collect(Collectors.groupingBy(Silla::getFila, LinkedHashMap::new, Collectors.toList()));
		Set<Long> vendidas = Set.copyOf(boletaRepository.buscarIdsSillasOcupadas(id, EstadoBoleta.CANCELADA));

		model.addAttribute("funcion", funcion);
		model.addAttribute("filas", filas);
		model.addAttribute("vendidas", vendidas);
		model.addAttribute("bloqueo", motivoBloqueo(funcion));
		return "sillas";
	}

	private String motivoBloqueo(Funcion funcion) {
		if (funcion.getEstado() == EstadoFuncion.CANCELADA) {
			return "La función fue cancelada";
		}
		if (!funcion.getFechaHoraInicio().isAfter(LocalDateTime.now())) {
			return "La función ya inició";
		}
		return null;
	}
}
