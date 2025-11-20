package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.audio.Music;

public class PantallaConfiguracion extends BaseScreen {
    private Stage stage;
    private Skin skin;
    private Music musica;
    private Slider slider;

    public PantallaConfiguracion(SpaceNavigation game) {
        super(game);
    }

    @Override
    protected void onShow() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label label = new Label("Volumen", skin);
        label.setPosition(550, 500);
        stage.addActor(label);

        // Crea el slider y ponle el valor global actual desde Settings
        slider = new Slider(0, 1, 0.01f, false, skin);
        slider.setValue(Settings.getInstance().getVolumen());
        slider.setSize(300, 50);
        slider.setPosition(450, 400);
        stage.addActor(slider);

        // Carga y reproduce la música
        musica = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        musica.setLooping(true);
        musica.setVolume(slider.getValue());
        musica.play();

        // Listener para cambiar volumen en tiempo real
        slider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float value = slider.getValue();
                musica.setVolume(value);     // Cambia el volumen de la música
                Settings.getInstance().setVolumen(value);      // Guarda el volumen global
            }
        });

        // Botón Volver
        TextButton volver = new TextButton("Volver", skin);
        volver.setSize(240, 60);
        volver.setPosition(480, 300);
        volver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musica.stop();         // Para la música al volver
                musica.dispose();
                game.setScreen(new PantallaMenu(game));
                dispose();
            }
        });
        stage.addActor(volver);
    }

    @Override
    protected void onRender(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    protected void onResize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    protected void onDispose() {
        stage.dispose();
        skin.dispose();
        if (musica != null) musica.dispose();
    }
}
