package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Movimiento controlado por teclado.
 */
public class KeyboardMovementStrategy implements MovementStrategy {

    private final float increment;

    public KeyboardMovementStrategy(float increment) {
        this.increment = increment;
    }

    @Override
    public void move(Nave4 nave) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) nave.modXVel(-increment);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) nave.modXVel(increment);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) nave.modYVel(-increment);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) nave.modYVel(increment);
    }
}
