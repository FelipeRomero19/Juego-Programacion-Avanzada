package puppy.code;

public interface Dañable {
	
	/*
	 * Interfaz que define que una entidad puede recibir daño y ser destruidad
	 * Implementada por Ball2 y Nave4
	 */
	
	// Aplica daño
	void daño(int cantidad);
	
	// consulta si la entidad esta destruida
	boolean isDestroyed();
}
