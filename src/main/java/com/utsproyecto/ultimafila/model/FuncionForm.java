package com.utsproyecto.ultimafila.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class FuncionForm {

	@NotNull(message = "Elige una película")
	private Long idPelicula;

	@NotNull(message = "Elige una sala")
	private Long idSala;

	@NotNull(message = "La fecha es obligatoria")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fecha;

	@NotNull(message = "La hora es obligatoria")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime hora;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0", inclusive = false, message = "El precio debe ser mayor que cero")
	private BigDecimal precio;

	public Long getIdPelicula() {
		return idPelicula;
	}

	public void setIdPelicula(Long idPelicula) {
		this.idPelicula = idPelicula;
	}

	public Long getIdSala() {
		return idSala;
	}

	public void setIdSala(Long idSala) {
		this.idSala = idSala;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
}
