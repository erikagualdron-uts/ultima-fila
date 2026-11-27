package com.utsproyecto.ultimafila.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.repository.BoletaRepository;

@Controller
public class AcomodadorController {

	private static final long MINUTOS_ANTES_DEL_INGRESO = 30;
	private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

	private final BoletaRepository boletaRepository;

	public AcomodadorController(BoletaRepository boletaRepository) {
		this.boletaRepository = boletaRepository;
	}

	// P-19: campo de código y resultado de la última validación
	@GetMapping("/acomodador")
	public String control() {
		return "acomodador";
	}

	// BR-CIN-006: una boleta VENDIDA se valida una sola vez, desde 30 min antes del inicio hasta el fin de su función.
	// Cualquier rechazo muestra el motivo y deja la boleta como estaba (REQ-CIN-020)
	@PostMapping("/acomodador/validar")
	@Transactional
	public String validar(@RequestParam String codigo, RedirectAttributes flash) {
		Optional<Boleta> encontrada = boletaRepository.buscarParaValidar(codigo.trim().toUpperCase());
		LocalDateTime ahora = LocalDateTime.now();
		boolean valida = false;
		String resultado;

		if (encontrada.isEmpty()) {
			resultado = "Boleta no encontrada";
		} else {
			Boleta boleta = encontrada.get();
			Funcion funcion = boleta.getFuncion();
			if (boleta.getEstado() == EstadoBoleta.CANCELADA) {
				resultado = "Boleta cancelada";
			} else if (boleta.getEstado() == EstadoBoleta.USADA) {
				resultado = "Boleta ya usada a las " + boleta.getFechaIngreso().format(HORA);
			} else if (ahora.isBefore(funcion.getFechaHoraInicio().minusMinutes(MINUTOS_ANTES_DEL_INGRESO))) {
				resultado = "Aún no se permite el ingreso";
			} else if (ahora.isAfter(funcion.getFechaHoraFin())) {
				resultado = "La función ya terminó";
			} else {
				boleta.setEstado(EstadoBoleta.USADA);
				boleta.setFechaIngreso(ahora);
				valida = true;
				resultado = funcion.getSala().getNombre() + ", silla " + boleta.getSilla().getFila()
						+ boleta.getSilla().getNumero();
			}
		}
		flash.addFlashAttribute("valida", valida);
		flash.addFlashAttribute("resultado", resultado);
		return "redirect:/acomodador";
	}
}
