package com.utsproyecto.ultimafila.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.Compra;
import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoCompra;
import com.utsproyecto.ultimafila.repository.BoletaRepository;
import com.utsproyecto.ultimafila.repository.CompraRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MisBoletasController {

	private static final long HORAS_DE_PLAZO = 2;

	private final BoletaRepository boletaRepository;
	private final CompraRepository compraRepository;

	public MisBoletasController(BoletaRepository boletaRepository, CompraRepository compraRepository) {
		this.boletaRepository = boletaRepository;
		this.compraRepository = compraRepository;
	}

	// P-07: el cliente ve solo sus compras, de la más reciente a la más antigua (NFR-CIN-002)
	@GetMapping("/mis-boletas")
	public String misBoletas(HttpSession sesion, Model model) {
		List<Boleta> boletas = boletaRepository.buscarDeCliente((Long) sesion.getAttribute("usuarioId"));
		Map<Compra, List<Boleta>> compras = boletas.stream()
				.collect(Collectors.groupingBy(Boleta::getCompra, LinkedHashMap::new, Collectors.toList()));
		model.addAttribute("compras", compras);
		return "mis-boletas";
	}

	// BR-CIN-004: se cancela una compra PAGADA hasta 2 horas antes de la función; las sillas quedan libres
	// porque la consulta de sillas ocupadas ignora las boletas CANCELADAS
	@PostMapping("/mis-boletas/{codigo}/cancelar")
	@Transactional
	public String cancelar(@PathVariable String codigo, HttpSession sesion, RedirectAttributes flash) {
		Compra compra = compraRepository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (compra.getCliente() == null || !Objects.equals(compra.getCliente().getId(), sesion.getAttribute("usuarioId"))) {
			return "redirect:/acceso-denegado";
		}
		if (compra.getEstado() != EstadoCompra.PAGADA) {
			flash.addFlashAttribute("error", "Solo se pueden cancelar compras pagadas");
			return "redirect:/mis-boletas";
		}
		List<Boleta> boletas = boletaRepository.buscarDeCompra(compra.getId());
		if (!dentroDelPlazo(boletas.get(0).getFuncion().getFechaHoraInicio(), LocalDateTime.now())) {
			flash.addFlashAttribute("error", "Plazo de cancelación vencido");
			return "redirect:/mis-boletas";
		}
		compra.setEstado(EstadoCompra.CANCELADA);
		boletas.forEach(boleta -> boleta.setEstado(EstadoBoleta.CANCELADA));
		flash.addFlashAttribute("mensaje", "Compra " + compra.getCodigo() + " cancelada. Las sillas quedaron libres.");
		return "redirect:/mis-boletas";
	}

	// Inicio de la función − 2 h ≥ ahora: justo 2 horas antes todavía se acepta
	static boolean dentroDelPlazo(LocalDateTime inicioFuncion, LocalDateTime ahora) {
		return !inicioFuncion.minusHours(HORAS_DE_PLAZO).isBefore(ahora);
	}
}
