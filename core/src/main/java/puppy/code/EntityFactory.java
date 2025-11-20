package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

/**
 * Abstract Factory: crea entidades del juego.
 */
public interface EntityFactory {
    Ball2 createBall(int x, int y, int size, int velX, int velY, Texture tx);
    Nave4 createNave(int x, int y, Texture tx, Sound hurt, Texture txBala, Sound shotSound, PantallaJuego juego);
    Bullet createBullet(float x, float y, int vx, int vy, Texture tx);
    VidaExtra createVidaExtra(float x, float y, int tamaño, Texture tx);
}
