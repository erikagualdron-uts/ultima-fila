package com.utsproyecto.ultimafila.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "peliculas")
public class Pelicula {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El título es obligatorio")
	@Size(max = 120, message = "Máximo 120 caracteres")
	@Column(nullable = false, length = 120)
	private String titulo;

	@NotBlank(message = "La sinopsis es obligatoria")
	@Size(max = 1000, message = "Máximo 1000 caracteres")
	@Column(nullable = false, length = 1000)
	private String sinopsis;

	@Min(value = 1, message = "La duración debe ser de al menos 1 minuto")
	@Column(name = "duracion_min", nullable = false)
	private int duracionMin;

	@NotNull(message = "Elige una clasificación")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Clasificacion clasificacion;

	@Size(max = 500, message = "Máximo 500 caracteres")
	@Column(name = "poster_url", length = 500)
	private String posterUrl;

	@Column(name = "en_cartelera", nullable = false)
	private boolean enCartelera = true;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_genero", nullable = false)
	private Genero genero;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSinopsis() {
		return sinopsis;
	}

	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	public int getDuracionMin() {
		return duracionMin;
	}

	public void setDuracionMin(int duracionMin) {
		this.duracionMin = duracionMin;
	}

	public Clasificacion getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(Clasificacion clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public boolean isEnCartelera() {
		return enCartelera;
	}

	public void setEnCartelera(boolean enCartelera) {
		this.enCartelera = enCartelera;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}
}
