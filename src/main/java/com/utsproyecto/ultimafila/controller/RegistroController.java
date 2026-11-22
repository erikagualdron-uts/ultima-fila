package com.utsproyecto.ultimafila.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.config.Claves;
import com.utsproyecto.ultimafila.model.RegistroForm;
import com.utsproyecto.ultimafila.model.Rol;
import com.utsproyecto.ultimafila.model.Usuario;
import com.utsproyecto.ultimafila.repository.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
public class RegistroController {

	private final UsuarioRepository usuarioRepository;

	public RegistroController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/registro")
	public String formulario(Model model) {
		model.addAttribute("registroForm", new RegistroForm());
		return "registro";
	}

	@PostMapping("/registro")
	public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form, BindingResult resultado,
			RedirectAttributes flash) {
		String correo = form.getCorreo() == null ? null : form.getCorreo().trim().toLowerCase();
		if (!resultado.hasFieldErrors("correo") && usuarioRepository.existsByCorreo(correo)) {
			resultado.rejectValue("correo", "correo.repetido", "Correo ya registrado");
		}
		if (resultado.hasErrors()) {
			return "registro";
		}

		Usuario usuario = new Usuario();
		usuario.setNombreCompleto(form.getNombreCompleto().trim());
		usuario.setDocumento(form.getDocumento().trim());
		usuario.setCorreo(correo);
		usuario.setPasswordHash(Claves.cifrar(form.getClave()));
		usuario.setRol(Rol.CLIENTE);
		usuario.setActivo(true);
		usuario.setFechaRegistro(LocalDateTime.now());
		usuarioRepository.save(usuario);

		flash.addFlashAttribute("mensaje", "Cuenta creada. Ya puedes iniciar sesión.");
		return "redirect:/login";
	}
}
