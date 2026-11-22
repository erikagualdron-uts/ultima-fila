package com.utsproyecto.ultimafila.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "salas")
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 60, message = "Máximo 60 caracteres")
	@Column(nullable = false, unique = true, length = 60)
	private String nombre;

	@Min(value = 1, message = "Mínimo 1 fila")
	@Max(value = 26, message = "Máximo 26 filas (letras A a Z)")
	@Column(nullable = false)
	private int filas;

	@Min(value = 1, message = "Mínimo 1 silla por fila")
	@Max(value = 30, message = "Máximo 30 sillas por fila")
	@Column(name = "sillas_por_fila", nullable = false)
	private int sillasPorFila;

	@Column(nullable = false)
	private boolean activa = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public int getSillasPorFila() {
		return sillasPorFila;
	}

	public void setSillasPorFila(int sillasPorFila) {
		this.sillasPorFila = sillasPorFila;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}
}
