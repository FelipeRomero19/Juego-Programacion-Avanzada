package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Screen;

public class PantallaGameOver extends BaseScreen {

    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    public PantallaGameOver(SpaceNavigation game) {
        super(game);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
    }

    @Override
    protected void onShow() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Genera la fuente TTF con acentos
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZáéíóúÁÉÍÓÚñÑüÜ0123456789,.!¡¿?:;+-*/()[]{}=<>\"' ";
        font = generator.generateFont(parameter);
        generator.dispose();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.add("default-font", font, BitmapFont.class);

        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;

        // Botón "Jugar de Nuevo"
        TextButton jugarDeNuevo = new TextButton("Jugar de Nuevo", style);
        jugarDeNuevo.setSize(350, 60);
        jugarDeNuevo.setPosition(425, 340);
        jugarDeNuevo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Screen ss = new PantallaJuego(game, 1, 3, 0, 1, 1, 10); 
                ss.resize(1200, 800);
                game.setScreen(ss);
                dispose();
            }
        });
        stage.addActor(jugarDeNuevo);

        // Botón "Volver al Menú"
        TextButton volverMenu = new TextButton("Volver al Menú", style);
        volverMenu.setSize(350, 60);
        volverMenu.setPosition(425, 240);
        volverMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PantallaMenu(game));
                dispose();
            }
        });
        stage.addActor(volverMenu);
    }

    @Override
    protected void onRender(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();

        GlyphLayout layout = new GlyphLayout();
        String gameOverText = "Game Over !!!";
        layout.setText(font, gameOverText);
        float xCenter = (1200 - layout.width) / 2;
        font.draw(game.getBatch(), gameOverText, xCenter, 500);

        game.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    protected void onResize(int width, int height) {
        if (stage != null) stage.getViewport().update(width, height, true);
    }

    @Override
    protected void onDispose() {
        stage.dispose();
        skin.dispose();
    }
}
