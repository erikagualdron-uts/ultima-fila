package com.utsproyecto.ultimafila.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.repository.BoletaRepository;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;

@Controller
public class AdminDashboardController {

	private final FuncionRepository funcionRepository;
	private final SillaRepository sillaRepository;
	private final BoletaRepository boletaRepository;

	public AdminDashboardController(FuncionRepository funcionRepository, SillaRepository sillaRepository,
			BoletaRepository boletaRepository) {
		this.funcionRepository = funcionRepository;
		this.sillaRepository = sillaRepository;
		this.boletaRepository = boletaRepository;
	}

	// P-09: funciones del día, boletas vendidas hoy (compradas hoy, sin cancelaciones), ingreso y ocupación por función
	@GetMapping("/admin")
	public String dashboard(Model model) {
		LocalDate hoy = LocalDate.now();
		LocalDateTime desde = hoy.atStartOfDay();
		LocalDateTime hasta = hoy.plusDays(1).atStartOfDay();

		List<Funcion> funciones = funcionRepository.buscarPorDia(EstadoFuncion.PROGRAMADA, desde, hasta);
		Map<Long, Integer> ocupacion = new LinkedHashMap<>();
		for (Funcion funcion : funciones) {
			long activas = sillaRepository.countBySalaIdAndActivaTrue(funcion.getSala().getId());
			long ocupadas = boletaRepository.countByFuncionIdAndEstadoNot(funcion.getId(), EstadoBoleta.CANCELADA);
			ocupacion.put(funcion.getId(), porcentaje(ocupadas, activas));
		}
		BigDecimal ingreso = boletaRepository.sumarIngresoEntre(EstadoBoleta.CANCELADA, desde, hasta);

		model.addAttribute("hoy", hoy);
		model.addAttribute("funciones", funciones);
		model.addAttribute("ocupacion", ocupacion);
		model.addAttribute("boletasVendidas", boletaRepository.contarVendidasEntre(EstadoBoleta.CANCELADA, desde, hasta));
		model.addAttribute("ingreso", ingreso != null ? ingreso : BigDecimal.ZERO);
		return "admin/dashboard";
	}

	// Entero entre 0 y 100; sin sillas activas o con más boletas que sillas activas no pasa de los extremos
	private static int porcentaje(long ocupadas, long activas) {
		if (activas <= 0) {
			return 0;
		}
		return (int) Math.min(100, Math.round(ocupadas * 100.0 / activas));
	}
}
