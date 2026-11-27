package com.utsproyecto.ultimafila.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.Compra;
import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoCompra;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.repository.BoletaRepository;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class TaquillaController {

	private final FuncionRepository funcionRepository;
	private final SillaRepository sillaRepository;
	private final BoletaRepository boletaRepository;

	public TaquillaController(FuncionRepository funcionRepository, SillaRepository sillaRepository,
			BoletaRepository boletaRepository) {
		this.funcionRepository = funcionRepository;
		this.sillaRepository = sillaRepository;
		this.boletaRepository = boletaRepository;
	}

	// P-18: funciones de hoy con sillas libres y las ventas del día del taquillero
	@GetMapping("/taquilla")
	public String panel(HttpSession sesion, Model model) {
		LocalDate hoy = LocalDate.now();
		LocalDateTime desde = hoy.atStartOfDay();
		LocalDateTime hasta = hoy.plusDays(1).atStartOfDay();

		List<Funcion> funciones = funcionRepository.buscarPorDia(EstadoFuncion.PROGRAMADA, desde, hasta);
		Map<Long, Long> libres = new LinkedHashMap<>();
		for (Funcion funcion : funciones) {
			long activas = sillaRepository.countBySalaIdAndActivaTrue(funcion.getSala().getId());
			long ocupadas = boletaRepository.countByFuncionIdAndEstadoNotAndSillaActivaTrue(funcion.getId(), EstadoBoleta.CANCELADA);
			libres.put(funcion.getId(), activas - ocupadas);
		}

		List<Boleta> boletas = boletaRepository.buscarVentasDelDia((Long) sesion.getAttribute("usuarioId"), desde, hasta);
		Map<Compra, List<Boleta>> ventas = boletas.stream()
				.collect(Collectors.groupingBy(Boleta::getCompra, LinkedHashMap::new, Collectors.toList()));
		BigDecimal totalVendido = ventas.keySet().stream().filter(c -> c.getEstado() == EstadoCompra.PAGADA)
				.map(Compra::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		model.addAttribute("funciones", funciones);
		model.addAttribute("libres", libres);
		model.addAttribute("ahora", LocalDateTime.now());
		model.addAttribute("ventas", ventas);
		model.addAttribute("totalVendido", totalVendido);
		return "taquilla";
	}
}
