package com.utsproyecto.ultimafila.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.model.Boleta;
import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.model.Silla;
import com.utsproyecto.ultimafila.repository.BoletaRepository;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;

@Controller
public class AcomodadorController {

	private static final long MINUTOS_ANTES_DEL_INGRESO = 30;
	private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

	private final BoletaRepository boletaRepository;
	private final FuncionRepository funcionRepository;
	private final SillaRepository sillaRepository;

	public AcomodadorController(BoletaRepository boletaRepository, FuncionRepository funcionRepository,
			SillaRepository sillaRepository) {
		this.boletaRepository = boletaRepository;
		this.funcionRepository = funcionRepository;
		this.sillaRepository = sillaRepository;
	}

	// P-19: campo de código, resultado de la última validación y lista de funciones del día
	@GetMapping("/acomodador")
	public String control(Model model) {
		LocalDate hoy = LocalDate.now();
		model.addAttribute("funcionesHoy",
				funcionRepository.buscarPorDia(EstadoFuncion.PROGRAMADA, hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay()));
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

	// P-20: mapa de una función del día con sillas libres, vendidas por ingresar e ingresadas y el total de cada estado
	@GetMapping("/acomodador/funciones/{id}")
	public String ocupacion(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Funcion funcion = funcionRepository.buscarConDetalle(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (funcion.getEstado() != EstadoFuncion.PROGRAMADA || !funcion.getFechaHoraInicio().toLocalDate().equals(LocalDate.now())) {
			flash.addFlashAttribute("error", "Solo se consulta la ocupación de funciones del día");
			return "redirect:/acomodador";
		}

		List<Silla> sillas = sillaRepository.findBySalaIdOrderByFilaAscNumeroAsc(funcion.getSala().getId());
		Map<Long, EstadoBoleta> boletas = boletaRepository.buscarVigentesDeFuncion(id, EstadoBoleta.CANCELADA).stream()
				.collect(Collectors.toMap(b -> b.getSilla().getId(), Boleta::getEstado));

		// La clase CSS y la etiqueta de cada silla; la silla con boleta se muestra con el estado de su boleta
		Map<Long, String> clases = new LinkedHashMap<>();
		Map<Long, String> etiquetas = new LinkedHashMap<>();
		int libres = 0;
		int porIngresar = 0;
		int ingresadas = 0;
		for (Silla silla : sillas) {
			EstadoBoleta boleta = boletas.get(silla.getId());
			if (boleta == EstadoBoleta.USADA) {
				clases.put(silla.getId(), "ingresada");
				etiquetas.put(silla.getId(), "ingresada");
				ingresadas++;
			} else if (boleta == EstadoBoleta.VENDIDA) {
				clases.put(silla.getId(), "vendida");
				etiquetas.put(silla.getId(), "vendida, por ingresar");
				porIngresar++;
			} else if (!silla.isActiva()) {
				clases.put(silla.getId(), "fuera");
				etiquetas.put(silla.getId(), "fuera de servicio");
			} else {
				clases.put(silla.getId(), "");
				etiquetas.put(silla.getId(), "libre");
				libres++;
			}
		}

		model.addAttribute("funcion", funcion);
		model.addAttribute("filas", sillas.stream()
				.collect(Collectors.groupingBy(Silla::getFila, LinkedHashMap::new, Collectors.toList())));
		model.addAttribute("clases", clases);
		model.addAttribute("etiquetas", etiquetas);
		model.addAttribute("libres", libres);
		model.addAttribute("porIngresar", porIngresar);
		model.addAttribute("ingresadas", ingresadas);
		return "ocupacion";
	}
}
