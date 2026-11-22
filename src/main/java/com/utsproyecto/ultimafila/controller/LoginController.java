package com.utsproyecto.ultimafila.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.config.Claves;
import com.utsproyecto.ultimafila.model.Rol;
import com.utsproyecto.ultimafila.model.Usuario;
import com.utsproyecto.ultimafila.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private final UsuarioRepository usuarioRepository;

	public LoginController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/login")
	public String formulario(HttpSession sesion) {
		if (sesion.getAttribute("rol") instanceof Rol rol) {
			return "redirect:" + panelDe(rol);
		}
		return "login";
	}

	@PostMapping("/login")
	public String iniciar(@RequestParam String correo, @RequestParam String clave, HttpServletRequest peticion,
			Model model) {
		Optional<Usuario> encontrado = usuarioRepository.findByCorreo(correo.trim().toLowerCase());
		if (encontrado.isEmpty() || !Claves.coincide(clave, encontrado.get().getPasswordHash())) {
			model.addAttribute("fallo", "Credenciales inválidas");
			model.addAttribute("correo", correo);
			return "login";
		}
		Usuario usuario = encontrado.get();
		if (!usuario.isActivo()) {
			model.addAttribute("fallo", "Cuenta inactiva");
			model.addAttribute("correo", correo);
			return "login";
		}

		peticion.getSession(true);
		peticion.changeSessionId();
		HttpSession sesion = peticion.getSession();
		sesion.setAttribute("usuarioId", usuario.getId());
		sesion.setAttribute("nombre", usuario.getNombreCompleto());
		sesion.setAttribute("rol", usuario.getRol());
		return "redirect:" + panelDe(usuario.getRol());
	}

	@PostMapping("/logout")
	public String cerrar(HttpSession sesion, RedirectAttributes flash) {
		sesion.invalidate();
		flash.addFlashAttribute("mensaje", "Sesión cerrada");
		return "redirect:/";
	}

	private String panelDe(Rol rol) {
		return switch (rol) {
		case ADMINISTRADOR -> "/admin";
		case TAQUILLERO -> "/taquilla";
		case ACOMODADOR -> "/acomodador";
		case CLIENTE -> "/";
		};
	}
}
