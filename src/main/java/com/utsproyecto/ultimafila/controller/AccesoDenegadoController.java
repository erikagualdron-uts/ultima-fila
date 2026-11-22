package com.utsproyecto.ultimafila.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AccesoDenegadoController {

	@GetMapping("/acceso-denegado")
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String accesoDenegado(Model model) {
		model.addAttribute("codigo", 403);
		model.addAttribute("titulo", "Acceso denegado");
		model.addAttribute("mensaje", "Tu rol no tiene permiso para entrar a esta página.");
		return "error";
	}
}
