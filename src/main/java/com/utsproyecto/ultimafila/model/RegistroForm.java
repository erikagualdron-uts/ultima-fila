package com.utsproyecto.ultimafila.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroForm {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 120, message = "Máximo 120 caracteres")
	private String nombreCompleto;

	@NotBlank(message = "El documento es obligatorio")
	@Size(max = 20, message = "Máximo 20 caracteres")
	private String documento;

	@NotBlank(message = "El correo es obligatorio")
	@Email(message = "Correo no válido")
	@Size(max = 120, message = "Máximo 120 caracteres")
	private String correo;

	@Size(min = 8, max = 100, message = "Mínimo 8 caracteres")
	private String clave;

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
}
