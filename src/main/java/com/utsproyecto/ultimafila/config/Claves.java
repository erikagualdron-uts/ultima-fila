package com.utsproyecto.ultimafila.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

// Clave guardada como "sal:hash", con hash = SHA-256(sal + clave) en hexadecimal
public final class Claves {

	private static final SecureRandom ALEATORIO = new SecureRandom();

	private Claves() {
	}

	public static String cifrar(String clave) {
		byte[] bytesSal = new byte[8];
		ALEATORIO.nextBytes(bytesSal);
		String sal = HexFormat.of().formatHex(bytesSal);
		return sal + ":" + hash(sal, clave);
	}

	public static boolean coincide(String clave, String guardado) {
		String[] partes = guardado.split(":", 2);
		return partes.length == 2 && MessageDigest.isEqual(
				hash(partes[0], clave).getBytes(StandardCharsets.UTF_8),
				partes[1].getBytes(StandardCharsets.UTF_8));
	}

	private static String hash(String sal, String clave) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			return HexFormat.of().formatHex(sha.digest((sal + clave).getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 no disponible", e);
		}
	}
}
