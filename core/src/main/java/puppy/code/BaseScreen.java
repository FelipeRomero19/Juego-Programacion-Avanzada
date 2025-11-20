package puppy.code;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Template Method: define esquema básico de una pantalla.
 * Subclases implementan hook methods: onShow,onUpdate,onRender,onHide y onDispose.
 */
public abstract class BaseScreen implements Screen {

    protected SpaceNavigation game;
    protected SpriteBatch batch;

    public BaseScreen(SpaceNavigation game) {
        this.game = game;
        this.batch = game.getBatch();
    }

    // hooks que las pantallas deben implementar
    protected void onShow() {}
    protected void onHide() {}
    protected void onDispose() {}
    protected void onResize(int width, int height) {}
    // update lógico del juego (por defecto vacío)
    protected void onUpdate(float delta) {}
    // render gráfico (por defecto vacío)
    protected void onRender(float delta) {}

    @Override
    public void show() {
        onShow();
    }

    // render llama a onUpdate y luego onRender
    @Override
    public final void render(float delta) {
        onUpdate(delta);
        onRender(delta);
    }

    @Override
    public void resize(int width, int height) {
        onResize(width, height);
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {
        onHide();
    }
    @Override
    public void dispose() {
        onDispose();
    }
}
