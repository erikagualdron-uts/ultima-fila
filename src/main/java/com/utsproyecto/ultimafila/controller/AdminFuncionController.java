package com.utsproyecto.ultimafila.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.model.FuncionForm;
import com.utsproyecto.ultimafila.model.Pelicula;
import com.utsproyecto.ultimafila.model.Sala;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.PeliculaRepository;
import com.utsproyecto.ultimafila.repository.SalaRepository;

import jakarta.validation.Valid;

@Controller
public class AdminFuncionController {

	private static final int MINUTOS_LIMPIEZA = 15;

	private final FuncionRepository funcionRepository;
	private final PeliculaRepository peliculaRepository;
	private final SalaRepository salaRepository;

	public AdminFuncionController(FuncionRepository funcionRepository, PeliculaRepository peliculaRepository,
			SalaRepository salaRepository) {
		this.funcionRepository = funcionRepository;
		this.peliculaRepository = peliculaRepository;
		this.salaRepository = salaRepository;
	}

	@GetMapping("/admin/funciones")
	public String listar(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam(defaultValue = "0") long sala, Model model) {
		LocalDate dia = fecha == null ? LocalDate.now() : fecha;
		model.addAttribute("funciones", funcionRepository.buscarParaAdmin(sala, fecha != null,
				dia.atStartOfDay(), dia.plusDays(1).atStartOfDay()));
		model.addAttribute("salas", salaRepository.findAllByOrderByNombre());
		model.addAttribute("fecha", fecha);
		model.addAttribute("idSala", sala);
		return "admin/funciones";
	}

	@GetMapping("/admin/funciones/nueva")
	public String nueva(Model model) {
		model.addAttribute("funcionForm", new FuncionForm());
		return formulario(model);
	}

	@PostMapping("/admin/funciones")
	public String guardar(@Valid @ModelAttribute("funcionForm") FuncionForm form, BindingResult errores, Model model,
			RedirectAttributes flash) {
		Pelicula pelicula = form.getIdPelicula() == null ? null
				: peliculaRepository.findById(form.getIdPelicula()).orElse(null);
		Sala sala = form.getIdSala() == null ? null : salaRepository.findById(form.getIdSala()).orElse(null);
		if (form.getIdPelicula() != null && pelicula == null) {
			errores.rejectValue("idPelicula", "inexistente", "Elige una película");
		}
		if (form.getIdSala() != null && sala == null) {
			errores.rejectValue("idSala", "inexistente", "Elige una sala");
		}
		if (!errores.hasFieldErrors("fecha") && !errores.hasFieldErrors("hora")
				&& !LocalDateTime.of(form.getFecha(), form.getHora()).isAfter(LocalDateTime.now())) {
			errores.rejectValue("fecha", "pasada", "La función debe ser futura");
		}
		if (errores.hasErrors()) {
			return formulario(model);
		}

		LocalDateTime inicio = LocalDateTime.of(form.getFecha(), form.getHora());
		Funcion funcion = new Funcion();
		funcion.setPelicula(pelicula);
		funcion.setSala(sala);
		funcion.setFechaHoraInicio(inicio);
		funcion.setFechaHoraFin(inicio.plusMinutes(pelicula.getDuracionMin() + MINUTOS_LIMPIEZA));
		funcion.setPrecio(form.getPrecio());
		funcionRepository.save(funcion);
		flash.addFlashAttribute("mensaje", "Función programada");
		return "redirect:/admin/funciones";
	}

	private String formulario(Model model) {
		model.addAttribute("peliculas", peliculaRepository.findAll(Sort.by("titulo")));
		model.addAttribute("salas", salaRepository.findByActivaTrueOrderByNombre());
		return "admin/funcion-form";
	}
}
