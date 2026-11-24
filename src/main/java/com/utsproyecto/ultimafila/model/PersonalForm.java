package com.utsproyecto.ultimafila.model;

import jakarta.validation.constraints.NotNull;

// Alta de personal: los mismos datos del registro más el rol (solo Taquillero o Acomodador)
public class PersonalForm extends RegistroForm {

	@NotNull(message = "Elige un rol")
	private Rol rol;

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
}
