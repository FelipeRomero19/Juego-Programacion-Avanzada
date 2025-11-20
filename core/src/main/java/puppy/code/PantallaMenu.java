package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout; 

public class PantallaMenu extends BaseScreen {
    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    public PantallaMenu(SpaceNavigation game) {
        super(game);
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
        font = generator.generateFont(parameter); // <-- Guarda la fuente en la variable de clase
        generator.dispose();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.add("default-font", font, BitmapFont.class);

        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;

        // Botón Jugar
        TextButton boton1 = new TextButton("Botón Jugar", style);
        boton1.setSize(240, 60);
        boton1.setPosition(480, 500);
        boton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PantallaJuego(game, 1, 3, 0, 1, 1, 10));
                dispose();
            }
        });
        stage.addActor(boton1);

        // Botón Configuración
        TextButton boton2 = new TextButton("Configuración", style);
        boton2.setSize(240, 60);
        boton2.setPosition(480, 400);
        boton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PantallaConfiguracion(game));
                dispose();
            }
        });
        stage.addActor(boton2);

        // Botón Salir
        TextButton boton3 = new TextButton("Salir", style);
        boton3.setSize(240, 60);
        boton3.setPosition(480, 300);
        boton3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(boton3);
    }

    @Override
    protected void onRender(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibuja el título centrado arriba de los botones
        String titulo = "Space Navigation";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, titulo);
        float xCenter = (1200 - layout.width) / 2;
        float yPos = 620; // Ajuste según preferencias

        game.getBatch().begin();
        font.draw(game.getBatch(), titulo, xCenter, yPos);
        game.getBatch().end();

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
        //font.dispose();
    }
}
