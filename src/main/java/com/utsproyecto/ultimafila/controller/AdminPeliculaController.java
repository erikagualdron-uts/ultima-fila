package com.utsproyecto.ultimafila.controller;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Clasificacion;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Genero;
import com.utsproyecto.ultimafila.model.Pelicula;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.GeneroRepository;
import com.utsproyecto.ultimafila.repository.PeliculaRepository;

import jakarta.validation.Valid;

@Controller
public class AdminPeliculaController {

	private static final int POR_PAGINA = 10;

	private final PeliculaRepository peliculaRepository;
	private final GeneroRepository generoRepository;
	private final FuncionRepository funcionRepository;

	public AdminPeliculaController(PeliculaRepository peliculaRepository, GeneroRepository generoRepository,
			FuncionRepository funcionRepository) {
		this.peliculaRepository = peliculaRepository;
		this.generoRepository = generoRepository;
		this.funcionRepository = funcionRepository;
	}

	@GetMapping("/admin/peliculas")
	public String listar(@RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "1") int pagina,
			Model model) {
		Page<Pelicula> peliculas = peliculaRepository.buscarParaAdmin("%" + q.trim().toLowerCase() + "%",
				PageRequest.of(Math.max(pagina, 1) - 1, POR_PAGINA));
		model.addAttribute("peliculas", peliculas);
		model.addAttribute("q", q);
		return "admin/peliculas";
	}

	@GetMapping("/admin/peliculas/nueva")
	public String nueva(Model model) {
		model.addAttribute("pelicula", new Pelicula());
		return formulario(model);
	}

	@GetMapping("/admin/peliculas/{id}/editar")
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("pelicula", peliculaRepository.buscarConGenero(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
		return formulario(model);
	}

	@PostMapping("/admin/peliculas")
	public String guardar(@Valid @ModelAttribute("pelicula") Pelicula pelicula, BindingResult errores, Model model,
			RedirectAttributes flash) {
		if (pelicula.getGenero() == null) {
			pelicula.setGenero(new Genero());
		}
		Long idGenero = pelicula.getGenero().getId();
		if (idGenero == null) {
			errores.rejectValue("genero.id", "obligatorio", "Elige un género");
		}
		if (errores.hasErrors()) {
			return formulario(model);
		}
		pelicula.setGenero(generoRepository.findById(idGenero).orElse(null));
		if (pelicula.getGenero() == null) {
			errores.rejectValue("genero.id", "inexistente", "Elige un género");
			return formulario(model);
		}
		pelicula.setTitulo(pelicula.getTitulo().trim());
		if (pelicula.getPosterUrl() != null && pelicula.getPosterUrl().isBlank()) {
			pelicula.setPosterUrl(null);
		}
		peliculaRepository.save(pelicula);
		flash.addFlashAttribute("mensaje", "Película guardada");
		return "redirect:/admin/peliculas";
	}

	@PostMapping("/admin/peliculas/{id}/eliminar")
	public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
		if (funcionRepository.contarFuturasDePelicula(id, EstadoFuncion.PROGRAMADA, LocalDateTime.now()) > 0) {
			flash.addFlashAttribute("error", "No se puede eliminar una película con funciones futuras");
			return "redirect:/admin/peliculas";
		}
		try {
			peliculaRepository.deleteById(id);
			flash.addFlashAttribute("mensaje", "Película eliminada");
		} catch (DataIntegrityViolationException e) {
			flash.addFlashAttribute("error", "No se puede eliminar una película con funciones registradas");
		}
		return "redirect:/admin/peliculas";
	}

	private String formulario(Model model) {
		model.addAttribute("generos", generoRepository.findAllByOrderByNombre());
		model.addAttribute("clasificaciones", Clasificacion.values());
		return "admin/pelicula-form";
	}
}
