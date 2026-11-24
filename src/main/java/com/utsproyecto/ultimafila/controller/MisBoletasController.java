package com.utsproyecto.ultimafila.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.Compra;
import com.utsproyecto.ultimafila.repository.BoletaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MisBoletasController {

	private final BoletaRepository boletaRepository;

	public MisBoletasController(BoletaRepository boletaRepository) {
		this.boletaRepository = boletaRepository;
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
}
