package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

/**
 * Implementación por defecto de EntityFactory.
 */
public class DefaultEntityFactory implements EntityFactory {

    @Override
    public Ball2 createBall(int x, int y, int size, int velX, int velY, Texture tx) {
        return new Ball2(x, y, size, velX, velY, tx);
    }

    @Override
    public Nave4 createNave(int x, int y, Texture tx, Sound hurt, Texture txBala, Sound shotSound, PantallaJuego juego) {
        return new Nave4(x, y, tx, hurt, txBala, shotSound, juego);
    }

    @Override
    public Bullet createBullet(float x, float y, int vx, int vy, Texture tx) {
        return new Bullet(x, y, vx, vy, tx);
    }

    @Override
    public VidaExtra createVidaExtra(float x, float y, int tamaño, Texture tx) {
        return new VidaExtra(x, y, tamaño, tx);
    }
}
