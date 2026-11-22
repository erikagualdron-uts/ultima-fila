package com.utsproyecto.ultimafila.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.PeliculaRepository;
import com.utsproyecto.ultimafila.repository.SalaRepository;

@Controller
public class InicioController {

	private static final int DESTACADAS = 4;

	private final PeliculaRepository peliculaRepository;
	private final FuncionRepository funcionRepository;
	private final SalaRepository salaRepository;

	public InicioController(PeliculaRepository peliculaRepository, FuncionRepository funcionRepository,
			SalaRepository salaRepository) {
		this.peliculaRepository = peliculaRepository;
		this.funcionRepository = funcionRepository;
		this.salaRepository = salaRepository;
	}

	@GetMapping("/")
	public String inicio(Model model) {
		LocalDate hoy = LocalDate.now();
		model.addAttribute("destacadas", peliculaRepository.buscarDestacadas(EstadoFuncion.PROGRAMADA,
				LocalDateTime.now(), PageRequest.of(0, DESTACADAS)));
		model.addAttribute("funcionesHoy", funcionRepository.buscarPorDia(EstadoFuncion.PROGRAMADA,
				hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay()));
		model.addAttribute("salas", salaRepository.findByActivaTrueOrderByNombre());
		return "inicio";
	}
}
