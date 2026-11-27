package com.utsproyecto.ultimafila;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UltimaFilaApplication {

	public static void main(String[] args) {
		// Hora de Colombia en cualquier servidor (Render corre en UTC): LocalDateTime.now() decide
		// «función del día», «ya inició», el plazo de cancelación y la ventana de ingreso (BR-004, 005, 006)
		TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
		SpringApplication.run(UltimaFilaApplication.class, args);
	}

}
