package com.utsproyecto.ultimafila.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ManejadorErrores {

	@ExceptionHandler(NoResourceFoundException.class)
	public ModelAndView noEncontrado() {
		return vistaError(HttpStatus.NOT_FOUND, "Página no encontrada",
				"La página que buscas no existe o fue movida.");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ModelAndView identificadorInvalido() {
		return noEncontrado();
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ModelAndView errorConEstado(ResponseStatusException e) {
		if (e.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
			return noEncontrado();
		}
		return errorInterno();
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView errorInterno() {
		return vistaError(HttpStatus.INTERNAL_SERVER_ERROR, "Algo salió mal",
				"Tuvimos un problema inesperado. Intenta de nuevo en unos minutos.");
	}

	private ModelAndView vistaError(HttpStatus estado, String titulo, String mensaje) {
		ModelAndView vista = new ModelAndView("error");
		vista.setStatus(estado);
		vista.addObject("codigo", estado.value());
		vista.addObject("titulo", titulo);
		vista.addObject("mensaje", mensaje);
		return vista;
	}
}
