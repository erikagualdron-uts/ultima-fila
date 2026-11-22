package com.utsproyecto.ultimafila.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.utsproyecto.ultimafila.model.Pelicula;
import com.utsproyecto.ultimafila.repository.PeliculaRepository;

@Controller
public class CarteleraController {

	private static final int POR_PAGINA = 8;

	private final PeliculaRepository peliculaRepository;

	public CarteleraController(PeliculaRepository peliculaRepository) {
		this.peliculaRepository = peliculaRepository;
	}

	@GetMapping("/")
	public String cartelera(@RequestParam(defaultValue = "1") int pagina, Model model) {
		int indice = Math.max(pagina, 1) - 1;
		Page<Pelicula> peliculas = peliculaRepository.buscarEnCartelera(PageRequest.of(indice, POR_PAGINA));
		if (peliculas.getTotalPages() > 0 && indice >= peliculas.getTotalPages()) {
			peliculas = peliculaRepository.buscarEnCartelera(PageRequest.of(peliculas.getTotalPages() - 1, POR_PAGINA));
		}
		model.addAttribute("peliculas", peliculas);
		return "cartelera";
	}
}
