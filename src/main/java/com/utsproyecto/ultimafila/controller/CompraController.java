package com.utsproyecto.ultimafila.controller;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

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
import com.utsproyecto.ultimafila.model.CanalCompra;
import com.utsproyecto.ultimafila.model.Compra;
import com.utsproyecto.ultimafila.model.EstadoBoleta;
import com.utsproyecto.ultimafila.model.EstadoFuncion;
import com.utsproyecto.ultimafila.model.Funcion;
import com.utsproyecto.ultimafila.model.Rol;
import com.utsproyecto.ultimafila.model.Silla;
import com.utsproyecto.ultimafila.model.Usuario;
import com.utsproyecto.ultimafila.repository.BoletaRepository;
import com.utsproyecto.ultimafila.repository.CompraRepository;
import com.utsproyecto.ultimafila.repository.FuncionRepository;
import com.utsproyecto.ultimafila.repository.SillaRepository;
import com.utsproyecto.ultimafila.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CompraController {

	private static final String LETRAS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
	private static final int MAXIMO_BOLETAS = 6;
	private static final SecureRandom ALEATORIO = new SecureRandom();

	private final FuncionRepository funcionRepository;
	private final SillaRepository sillaRepository;
	private final CompraRepository compraRepository;
	private final BoletaRepository boletaRepository;
	private final UsuarioRepository usuarioRepository;

	public CompraController(FuncionRepository funcionRepository, SillaRepository sillaRepository,
			CompraRepository compraRepository, BoletaRepository boletaRepository,
			UsuarioRepository usuarioRepository) {
		this.funcionRepository = funcionRepository;
		this.sillaRepository = sillaRepository;
		this.compraRepository = compraRepository;
		this.boletaRepository = boletaRepository;
		this.usuarioRepository = usuarioRepository;
	}

	// P-06: resumen de la función y las sillas elegidas antes de pagar (solo Cliente)
	@GetMapping("/compras/confirmar")
	public String resumen(@RequestParam Long funcion, @RequestParam(required = false) List<Long> sillas,
			HttpSession sesion, Model model, RedirectAttributes flash) {
		Rol rol = (Rol) sesion.getAttribute("rol");
		if (rol != Rol.CLIENTE && rol != Rol.TAQUILLERO) {
			return "redirect:/acceso-denegado";
		}
		Funcion laFuncion = buscarFuncion(funcion);
		List<Silla> elegidas = sillasValidas(laFuncion, sillas);
		if (elegidas == null) {
			flash.addFlashAttribute("error", "Elige al menos una silla disponible");
			return "redirect:/funciones/" + funcion + "/sillas";
		}
		String motivo = motivoRechazo(laFuncion, elegidas, rol);
		if (motivo != null) {
			flash.addFlashAttribute("error", motivo);
			return "redirect:/funciones/" + funcion + "/sillas";
		}
		model.addAttribute("funcion", laFuncion);
		model.addAttribute("sillas", elegidas);
		model.addAttribute("total", laFuncion.getPrecio().multiply(BigDecimal.valueOf(elegidas.size())));
		return "confirmar";
	}

	// Pago simulado del cliente o efectivo en taquilla: crea la compra y una boleta por silla en una sola transacción
	@PostMapping("/compras/confirmar")
	@Transactional
	public String pagar(@RequestParam Long funcion, @RequestParam(required = false) List<Long> sillas,
			HttpSession sesion, RedirectAttributes flash) {
		Rol rol = (Rol) sesion.getAttribute("rol");
		if (rol != Rol.CLIENTE && rol != Rol.TAQUILLERO) {
			return "redirect:/acceso-denegado";
		}
		Funcion laFuncion = funcionRepository.buscarConBloqueo(funcion)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<Silla> elegidas = sillasValidas(laFuncion, sillas);
		if (elegidas == null) {
			flash.addFlashAttribute("error", "Elige al menos una silla disponible");
			return "redirect:/funciones/" + funcion + "/sillas";
		}
		String motivo = motivoRechazo(laFuncion, elegidas, rol);
		if (motivo != null) {
			flash.addFlashAttribute("error", motivo);
			return "redirect:/funciones/" + funcion + "/sillas";
		}
		Usuario usuario = usuarioRepository.findById((Long) sesion.getAttribute("usuarioId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		boolean enTaquilla = rol == Rol.TAQUILLERO;

		Compra compra = new Compra();
		compra.setCodigo(codigoUnico("C", compraRepository::existsByCodigo));
		compra.setFechaCompra(LocalDateTime.now());
		compra.setTotal(laFuncion.getPrecio().multiply(BigDecimal.valueOf(elegidas.size())));
		compra.setCanal(enTaquilla ? CanalCompra.TAQUILLA : CanalCompra.EN_LINEA);
		if (enTaquilla) {
			compra.setVendedor(usuario);
		} else {
			compra.setCliente(usuario);
		}
		compraRepository.save(compra);

		List<Boleta> boletas = new ArrayList<>();
		for (Silla silla : elegidas) {
			Boleta boleta = new Boleta();
			boleta.setCodigo(codigoUnico("B", boletaRepository::existsByCodigo));
			boleta.setPrecio(laFuncion.getPrecio());
			boleta.setCompra(compra);
			boleta.setFuncion(laFuncion);
			boleta.setSilla(silla);
			boletas.add(boleta);
		}
		boletaRepository.saveAll(boletas);

		flash.addFlashAttribute("mensaje",
				enTaquilla ? "Venta registrada en efectivo" : "Pago aprobado (simulado). ¡Disfruta la función!");
		return "redirect:/compras/" + compra.getCodigo();
	}

	// P-08: solo el cliente de la compra (o el taquillero que la vendió) ve el comprobante
	@GetMapping("/compras/{codigo}")
	public String comprobante(@PathVariable String codigo, HttpSession sesion, Model model) {
		Compra compra = compraRepository.buscarPorCodigo(codigo)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Object usuarioId = sesion.getAttribute("usuarioId");
		boolean propietario = (compra.getCliente() != null && Objects.equals(compra.getCliente().getId(), usuarioId))
				|| (compra.getVendedor() != null && Objects.equals(compra.getVendedor().getId(), usuarioId));
		if (!propietario) {
			return "redirect:/acceso-denegado";
		}
		List<Boleta> boletas = boletaRepository.buscarDeCompra(compra.getId());
		model.addAttribute("compra", compra);
		model.addAttribute("boletas", boletas);
		model.addAttribute("funcion", boletas.get(0).getFuncion());
		return "comprobante";
	}

	private Funcion buscarFuncion(Long id) {
		return funcionRepository.buscarConDetalle(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	// Devuelve las sillas pedidas si existen, son de la sala de la función y están activas; si no, null
	private List<Silla> sillasValidas(Funcion funcion, List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		List<Long> distintos = ids.stream().distinct().toList();
		List<Silla> sillas = sillaRepository.findAllById(distintos);
		boolean todasValidas = sillas.size() == distintos.size() && sillas.stream()
				.allMatch(s -> s.isActiva() && s.getSala().getId().equals(funcion.getSala().getId()));
		return todasValidas ? sillas : null;
	}

	// Reglas de compra: función iniciada o cancelada (BR-005), máximo 6 boletas (BR-002), silla vendida (BR-001);
	// en taquilla solo se venden funciones del día
	private String motivoRechazo(Funcion funcion, List<Silla> sillas, Rol rol) {
		if (funcion.getEstado() == EstadoFuncion.CANCELADA) {
			return "La función fue cancelada";
		}
		if (!funcion.getFechaHoraInicio().isAfter(LocalDateTime.now())) {
			return "La función ya inició";
		}
		if (rol == Rol.TAQUILLERO && !funcion.getFechaHoraInicio().toLocalDate().equals(LocalDate.now())) {
			return "En taquilla solo se venden funciones de hoy";
		}
		if (sillas.size() > MAXIMO_BOLETAS) {
			return "Máximo 6 boletas por compra";
		}
		Set<Long> vendidas = Set.copyOf(boletaRepository.buscarIdsSillasOcupadas(funcion.getId(), EstadoBoleta.CANCELADA));
		return sillas.stream().filter(s -> vendidas.contains(s.getId()))
				.min(Comparator.comparing(Silla::getFila).thenComparing(Silla::getNumero))
				.map(s -> "La silla " + s.getFila() + s.getNumero() + " ya fue vendida").orElse(null);
	}

	private String codigoUnico(String prefijo, Predicate<String> existe) {
		String codigo;
		do {
			StringBuilder aleatorio = new StringBuilder();
			for (int i = 0; i < 8; i++) {
				aleatorio.append(LETRAS.charAt(ALEATORIO.nextInt(LETRAS.length())));
			}
			codigo = prefijo + "-" + aleatorio;
		} while (existe.test(codigo));
		return codigo;
	}
}
