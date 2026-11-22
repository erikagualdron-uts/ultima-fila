package com.utsproyecto.ultimafila.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.model.Pelicula;
import com.utsproyecto.ultimafila.repository.BoletaRepository;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.PeliculaRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;

@Controller
public class PeliculaController {

	private static final long POCAS_SILLAS = 10;

	private final PeliculaRepository peliculaRepository;
	private final FuncionRepository funcionRepository;
	private final SillaRepository sillaRepository;
	private final BoletaRepository boletaRepository;

	public PeliculaController(PeliculaRepository peliculaRepository, FuncionRepository funcionRepository,
			SillaRepository sillaRepository, BoletaRepository boletaRepository) {
		this.peliculaRepository = peliculaRepository;
		this.funcionRepository = funcionRepository;
		this.sillaRepository = sillaRepository;
		this.boletaRepository = boletaRepository;
	}

	@GetMapping("/peliculas/{id}")
	public String detalle(@PathVariable Long id, Model model) {
		Pelicula pelicula = peliculaRepository.buscarConGenero(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Funcion> funciones = funcionRepository.buscarFuturasDePelicula(id, EstadoFuncion.PROGRAMADA,
				LocalDateTime.now());
		Map<Long, Long> disponibles = new HashMap<>();
		for (Funcion f : funciones) {
			long activas = sillaRepository.countBySalaIdAndActivaTrue(f.getSala().getId());
			long ocupadas = boletaRepository.countByFuncionIdAndEstadoNot(f.getId(), EstadoBoleta.CANCELADA);
			disponibles.put(f.getId(), activas - ocupadas);
		}
		model.addAttribute("pelicula", pelicula);
		model.addAttribute("funciones", funciones);
		model.addAttribute("disponibles", disponibles);
		model.addAttribute("pocasSillas", POCAS_SILLAS);
		return "pelicula";
	}
}
