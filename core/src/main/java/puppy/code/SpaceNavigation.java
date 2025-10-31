package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceNavigation extends Game {
    private String nombreJuego = "Space Navigation";
    private SpriteBatch batch;
    private BitmapFont font;
    private int highScore;
    private float volumen = 1f; // Volumen global, valor entre 0 y 1

    @Override
    public void create() {
        highScore = 0;
        batch = new SpriteBatch();
        font = new BitmapFont(); // usa Arial font x defecto
        font.getData().setScale(2f);

        // Pantalla de botones, puedes cambiar a PantallaMenu si lo prefieres
        Screen ss = new PantallaMenu(this);
        this.setScreen(ss);
    }

    @Override
    public void render() {
        super.render(); // important!
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    // Volumen global para música y efectos
    public float getVolumen() {
        return volumen;
    }

    public void setVolumen(float volumen) {
        this.volumen = volumen;
    }
}