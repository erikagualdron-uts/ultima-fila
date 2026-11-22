package com.utsproyecto.ultimafila.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Genero;
import com.utsproyecto.ultimafila.repository.GeneroRepository;

import jakarta.validation.Valid;

@Controller
public class AdminGeneroController {

	private final GeneroRepository generoRepository;

	public AdminGeneroController(GeneroRepository generoRepository) {
		this.generoRepository = generoRepository;
	}

	@GetMapping("/admin/generos")
	public String listar(@RequestParam(required = false) Long editar, Model model) {
		Genero genero = editar == null ? null : generoRepository.findById(editar).orElse(null);
		model.addAttribute("genero", genero != null ? genero : new Genero());
		return vista(model);
	}

	@PostMapping("/admin/generos")
	public String guardar(@Valid @ModelAttribute("genero") Genero genero, BindingResult errores, Model model,
			RedirectAttributes flash) {
		long id = genero.getId() == null ? 0 : genero.getId();
		if (!errores.hasFieldErrors("nombre") && generoRepository.existeNombre(genero.getNombre().trim(), id)) {
			errores.rejectValue("nombre", "repetido", "Ya existe un género con ese nombre");
		}
		if (errores.hasErrors()) {
			return vista(model);
		}
		genero.setNombre(genero.getNombre().trim());
		generoRepository.save(genero);
		flash.addFlashAttribute("mensaje", "Género guardado");
		return "redirect:/admin/generos";
	}

	@PostMapping("/admin/generos/{id}/eliminar")
	public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
		if (generoRepository.contarPeliculas(id) > 0) {
			flash.addFlashAttribute("error", "No se puede eliminar un género que tiene películas");
		} else {
			generoRepository.deleteById(id);
			flash.addFlashAttribute("mensaje", "Género eliminado");
		}
		return "redirect:/admin/generos";
	}

	private String vista(Model model) {
		model.addAttribute("generos", generoRepository.findAllByOrderByNombre());
		return "admin/generos";
	}
}
