package puppy.code;

/**
 * Strategy: define cómo se mueve la nave.
 * Implementaciones: KeyboardMovementStrategy.
 */
public interface MovementStrategy {
    void move(Nave4 nave);
}
