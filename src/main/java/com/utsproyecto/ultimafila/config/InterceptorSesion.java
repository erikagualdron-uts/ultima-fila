package com.utsproyecto.ultimafila.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.utsproyecto.ultimafila.model.Rol;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Protege las rutas por rol: sin sesión va a /login, con otro rol va a /acceso-denegado
public class InterceptorSesion implements HandlerInterceptor {

	private static final Map<String, List<Rol>> ROLES_POR_PREFIJO = Map.of(
			"/admin", List.of(Rol.ADMINISTRADOR),
			"/taquilla", List.of(Rol.TAQUILLERO),
			"/acomodador", List.of(Rol.ACOMODADOR),
			"/mis-boletas", List.of(Rol.CLIENTE),
			"/compras", List.of(Rol.CLIENTE, Rol.TAQUILLERO));

	public static String[] prefijosProtegidos() {
		return ROLES_POR_PREFIJO.keySet().stream().flatMap(p -> Stream.of(p, p + "/**"))
				.toArray(String[]::new);
	}

	@Override
	public boolean preHandle(HttpServletRequest peticion, HttpServletResponse respuesta, Object manejador)
			throws Exception {
		var sesion = peticion.getSession(false);
		if (sesion == null || !(sesion.getAttribute("rol") instanceof Rol rol)) {
			FlashMap aviso = new FlashMap();
			aviso.put("mensaje", "Inicia sesión para continuar");
			RequestContextUtils.getFlashMapManager(peticion).saveOutputFlashMap(aviso, peticion, respuesta);
			respuesta.sendRedirect(peticion.getContextPath() + "/login");
			return false;
		}
		if (!rolesPermitidos(peticion).contains(rol)) {
			respuesta.sendRedirect(peticion.getContextPath() + "/acceso-denegado");
			return false;
		}
		return true;
	}

	private List<Rol> rolesPermitidos(HttpServletRequest peticion) {
		String ruta = peticion.getRequestURI().substring(peticion.getContextPath().length());
		return ROLES_POR_PREFIJO.entrySet().stream()
				.filter(e -> ruta.equals(e.getKey()) || ruta.startsWith(e.getKey() + "/"))
				.map(Map.Entry::getValue).findFirst().orElse(List.of());
	}
}
