package com.utsproyecto.ultimafila.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Sala;
import com.utsproyecto.ultimafila.model.Silla;
import com.utsproyecto.ultimafila.repository.SalaRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;

import jakarta.validation.Valid;

@Controller
public class AdminSalaController {

	private final SalaRepository salaRepository;
	private final SillaRepository sillaRepository;

	public AdminSalaController(SalaRepository salaRepository, SillaRepository sillaRepository) {
		this.salaRepository = salaRepository;
		this.sillaRepository = sillaRepository;
	}

	@GetMapping("/admin/salas")
	public String listar(Model model) {
		model.addAttribute("sala", new Sala());
		return vista(model);
	}

	// La sala y todas sus sillas se guardan juntas: o se crean todas o ninguna
	@PostMapping("/admin/salas")
	@Transactional
	public String guardar(@Valid @ModelAttribute("sala") Sala sala, BindingResult errores, Model model,
			RedirectAttributes flash) {
		if (!errores.hasFieldErrors("nombre") && salaRepository.existsByNombreIgnoreCase(sala.getNombre().trim())) {
			errores.rejectValue("nombre", "repetido", "Nombre de sala repetido");
		}
		if (errores.hasErrors()) {
			return vista(model);
		}
		sala.setId(null);
		sala.setNombre(sala.getNombre().trim());
		sala.setActiva(true);
		salaRepository.save(sala);

		List<Silla> sillas = new ArrayList<>();
		for (int f = 0; f < sala.getFilas(); f++) {
			for (int n = 1; n <= sala.getSillasPorFila(); n++) {
				Silla silla = new Silla();
				silla.setFila(String.valueOf((char) ('A' + f)));
				silla.setNumero(n);
				silla.setActiva(true);
				silla.setSala(sala);
				sillas.add(silla);
			}
		}
		sillaRepository.saveAll(sillas);
		flash.addFlashAttribute("mensaje", "Sala creada con " + sillas.size() + " sillas");
		return "redirect:/admin/salas";
	}

	private String vista(Model model) {
		model.addAttribute("salas", salaRepository.findAllByOrderByNombre());
		return "admin/salas";
	}
}
