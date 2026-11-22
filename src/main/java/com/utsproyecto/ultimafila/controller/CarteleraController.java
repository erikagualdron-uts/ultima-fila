package com.utsproyecto.ultimafila.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Pelicula;
import com.utsproyecto.ultimafila.repository.GeneroRepository;
import com.utsproyecto.ultimafila.repository.PeliculaRepository;

@Controller
public class CarteleraController {

	private static final int POR_PAGINA = 8;

	private final PeliculaRepository peliculaRepository;
	private final GeneroRepository generoRepository;

	public CarteleraController(PeliculaRepository peliculaRepository, GeneroRepository generoRepository) {
		this.peliculaRepository = peliculaRepository;
		this.generoRepository = generoRepository;
	}

	@GetMapping("/cartelera")
	public String cartelera(@RequestParam(defaultValue = "1") int pagina,
			@RequestParam(defaultValue = "") String q,
			@RequestParam(required = false) Long genero,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			Model model) {
		String titulo = "%" + q.trim().toLowerCase() + "%";
		long idGenero = genero == null ? 0 : genero;
		LocalDate dia = fecha == null ? LocalDate.now() : fecha;
		LocalDateTime desde = dia.atStartOfDay();
		LocalDateTime hasta = dia.plusDays(1).atStartOfDay();

		int indice = Math.max(pagina, 1) - 1;
		Page<Pelicula> peliculas = buscar(titulo, idGenero, fecha != null, desde, hasta, indice);
		if (peliculas.getTotalPages() > 0 && indice >= peliculas.getTotalPages()) {
			peliculas = buscar(titulo, idGenero, fecha != null, desde, hasta, peliculas.getTotalPages() - 1);
		}
		model.addAttribute("peliculas", peliculas);
		model.addAttribute("generos", generoRepository.findAll());
		model.addAttribute("q", q);
		model.addAttribute("genero", genero);
		model.addAttribute("fecha", fecha);
		return "cartelera";
	}

	private Page<Pelicula> buscar(String titulo, long idGenero, boolean conFecha, LocalDateTime desde,
			LocalDateTime hasta, int indice) {
		return peliculaRepository.buscarEnCartelera(titulo, idGenero, conFecha, EstadoFuncion.PROGRAMADA, desde, hasta,
				PageRequest.of(indice, POR_PAGINA));
	}
}
