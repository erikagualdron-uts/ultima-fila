package com.utsproyecto.ultimafila.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Sala;
import com.utsproyecto.ultimafila.model.Silla;
import com.utsproyecto.ultimafila.repository.SalaRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;

@Controller
public class AdminSillaController {

	private final SalaRepository salaRepository;
	private final SillaRepository sillaRepository;

	public AdminSillaController(SalaRepository salaRepository, SillaRepository sillaRepository) {
		this.salaRepository = salaRepository;
		this.sillaRepository = sillaRepository;
	}

	@GetMapping("/admin/salas/{id}/sillas")
	public String mapa(@PathVariable Long id, Model model) {
		Sala sala = salaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Silla> sillas = sillaRepository.findBySalaIdOrderByFilaAscNumeroAsc(id);
		Map<String, List<Silla>> filas = sillas.stream()
				.collect(Collectors.groupingBy(Silla::getFila, LinkedHashMap::new, Collectors.toList()));
		model.addAttribute("sala", sala);
		model.addAttribute("filas", filas);
		return "admin/sillas";
	}

	// Alterna la silla entre activa y fuera de servicio; la compra ya excluye las sillas inactivas (BR-CIN-008)
	@PostMapping("/admin/salas/{id}/sillas/{idSilla}/alternar")
	public String alternar(@PathVariable Long id, @PathVariable Long idSilla, RedirectAttributes flash) {
		Silla silla = sillaRepository.findById(idSilla).filter(s -> s.getSala().getId().equals(id))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		silla.setActiva(!silla.isActiva());
		sillaRepository.save(silla);
		String etiqueta = silla.getFila() + silla.getNumero();
		flash.addFlashAttribute("mensaje",
				silla.isActiva() ? "Silla " + etiqueta + " activa" : "Silla " + etiqueta + " fuera de servicio");
		return "redirect:/admin/salas/" + id + "/sillas";
	}
}
