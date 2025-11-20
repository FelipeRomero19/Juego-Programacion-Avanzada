package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceNavigation extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    // highScore y volumen ahora en Settings singleton

    @Override
    public void create() {
        Settings.getInstance().setVolumen(1f);
        Settings.getInstance().setHighScore(0);

        batch = new SpriteBatch();
        font = new BitmapFont(); // usa Arial font x defecto
        font.getData().setScale(2f);

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

    // helper para obtener highscore/volumen desde Settings
    public int getHighScore() {
        return Settings.getInstance().getHighScore();
    }

    public void setHighScore(int s) {
        Settings.getInstance().setHighScore(s);
    }

    public float getVolumen() {
        return Settings.getInstance().getVolumen();
    }

    public void setVolumen(float v) {
        Settings.getInstance().setVolumen(v);
    }
}
