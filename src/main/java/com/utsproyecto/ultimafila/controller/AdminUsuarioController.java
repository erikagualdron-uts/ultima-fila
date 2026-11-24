package com.utsproyecto.ultimafila.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utsproyecto.ultimafila.config.Claves;
import com.utsproyecto.ultimafila.model.PersonalForm;
import com.utsproyecto.ultimafila.model.Rol;
import com.utsproyecto.ultimafila.model.Usuario;
import com.utsproyecto.ultimafila.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AdminUsuarioController {

	private static final List<Rol> ROLES_DE_PERSONAL = List.of(Rol.TAQUILLERO, Rol.ACOMODADOR);

	private final UsuarioRepository usuarioRepository;

	public AdminUsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	// P-17: listado con filtro por rol y formulario de alta de personal
	@GetMapping("/admin/usuarios")
	public String listar(@RequestParam(required = false) Rol rol, Model model) {
		model.addAttribute("personalForm", new PersonalForm());
		return vista(rol, model);
	}

	@PostMapping("/admin/usuarios")
	public String crear(@Valid @ModelAttribute("personalForm") PersonalForm form, BindingResult errores, Model model,
			RedirectAttributes flash) {
		String correo = form.getCorreo() == null ? null : form.getCorreo().trim().toLowerCase();
		if (!errores.hasFieldErrors("correo") && usuarioRepository.existsByCorreo(correo)) {
			errores.rejectValue("correo", "correo.repetido", "Correo ya registrado");
		}
		if (!errores.hasFieldErrors("rol") && !ROLES_DE_PERSONAL.contains(form.getRol())) {
			errores.rejectValue("rol", "rol.invalido", "Elige Taquillero o Acomodador");
		}
		if (errores.hasErrors()) {
			return vista(null, model);
		}

		Usuario usuario = new Usuario();
		usuario.setNombreCompleto(form.getNombreCompleto().trim());
		usuario.setDocumento(form.getDocumento().trim());
		usuario.setCorreo(correo);
		usuario.setPasswordHash(Claves.cifrar(form.getClave()));
		usuario.setRol(form.getRol());
		usuario.setActivo(true);
		usuario.setFechaRegistro(LocalDateTime.now());
		usuarioRepository.save(usuario);

		flash.addFlashAttribute("mensaje", "Cuenta creada: " + usuario.getNombreCompleto());
		return "redirect:/admin/usuarios";
	}

	// Activa o inactiva la cuenta; nunca se borra, así sus ventas se conservan
	@PostMapping("/admin/usuarios/{id}/estado")
	public String cambiarEstado(@PathVariable Long id, HttpSession sesion, RedirectAttributes flash) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (Objects.equals(usuario.getId(), sesion.getAttribute("usuarioId"))) {
			flash.addFlashAttribute("error", "No puedes inactivar tu propia cuenta");
			return "redirect:/admin/usuarios";
		}
		usuario.setActivo(!usuario.isActivo());
		usuarioRepository.save(usuario);
		flash.addFlashAttribute("mensaje",
				"Cuenta de " + usuario.getNombreCompleto() + (usuario.isActivo() ? " activada" : " inactivada"));
		return "redirect:/admin/usuarios";
	}

	private String vista(Rol rol, Model model) {
		model.addAttribute("usuarios", rol == null ? usuarioRepository.findAllByOrderByRolAscNombreCompletoAsc()
				: usuarioRepository.findByRolOrderByNombreCompleto(rol));
		model.addAttribute("rolFiltro", rol);
		model.addAttribute("roles", Rol.values());
		model.addAttribute("rolesDePersonal", ROLES_DE_PERSONAL);
		return "admin/usuarios";
	}
}
