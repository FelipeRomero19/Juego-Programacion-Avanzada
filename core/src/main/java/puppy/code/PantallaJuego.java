package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PantallaJuego extends BaseScreen {

    private OrthographicCamera camera;    
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides; 
    private int velYAsteroides; 
    private int cantAsteroides;
    
    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    // vida extra
    private VidaExtra vidaExtra;
    private float vidaExtraX, vidaExtraY;
    private int vidaExtraTamaño = 40;
    private Random rand = new Random();

    // escudo extra
    private EscudoExtra escudoExtra;
    private float escudoExtraX, escudoExtraY;
    private int escudoExtraTamaño = 40;
    private boolean shieldActivo = false;
    private float shieldTimer = 0f;
    private final float SHIELD_DURATION = 4f; // segundos

    private ShapeRenderer shapeRenderer;

    private EntityFactory factory;

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        super(game);
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1,0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        
        gameMusic.setLooping(true);
        gameMusic.setVolume(Settings.getInstance().getVolumen());
        gameMusic.play();
        
        factory = new DefaultEntityFactory();
        
        nave = factory.createNave(Gdx.graphics.getWidth()/2-50,30,
                new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                new Texture(Gdx.files.internal("Rocket2.png")), 
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")), this);
        nave.setVidas(vidas);

        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = factory.createBall(
                    r.nextInt((int)Gdx.graphics.getWidth()),
                    50+r.nextInt((int)Gdx.graphics.getHeight()-50),
                    20+r.nextInt(10),
                    velXAsteroides+r.nextInt(4),
                    velYAsteroides+r.nextInt(4),
                    new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );
            balls1.add(bb);
            balls2.add(bb);
        }

        // vida extra con factory + colocar
        vidaExtra = factory.createVidaExtra(0,0, vidaExtraTamaño, new Texture(Gdx.files.internal("vida-Extra.png")));
        colocarVidaExtraAleatoria();

        // escudo extra con factory + colocar 
        escudoExtra = factory.createEscudoExtra(
            rand.nextInt(Math.max(1, Gdx.graphics.getWidth() - escudoExtraTamaño)),
            100 + rand.nextInt(Math.max(1, Gdx.graphics.getHeight() - escudoExtraTamaño - 100)),
            escudoExtraTamaño,
            new Texture(Gdx.files.internal("shield.png")) // Si tienes un icono, sino pon alguna textura temporal
        );
        escudoExtra.setVisible(true);

        shapeRenderer = new ShapeRenderer();
    }

    private void colocarVidaExtraAleatoria() {
        vidaExtraX = rand.nextInt(Math.max(1, Gdx.graphics.getWidth() - vidaExtraTamaño));
        vidaExtraY = 100 + rand.nextInt(Math.max(1, Gdx.graphics.getHeight() - vidaExtraTamaño - 100));
        vidaExtra.setPosition(vidaExtraX, vidaExtraY);
        vidaExtra.setVisible(true);
    }
    
    public void dibujaEncabezado() {
        CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
        game.getFont().draw(batch, "HighScore:"+Settings.getInstance().getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
    }
    
    @Override
    protected void onUpdate(float delta) {
        if (!nave.estaHerido()) {
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {
                    Ball2 target = balls1.get(j);
                    if (b.checkCollision(target)) {
                        explosionSound.play(Settings.getInstance().getVolumen());
                        if(target.isDestroyed()) {
                            balls1.remove(j);
                            balls2.remove(j);
                            j--;
                            score +=10;
                        }
                    }      
                }
                if (b.isDestroyed()) {
                    balas.remove(b);
                    i--;
                }
            }
            for (Ball2 ball : balls1) {
                ball.update();
            }
            for (int i=0;i<balls1.size();i++) {
                Ball2 ball1 = balls1.get(i);   
                for (int j=0;j<balls2.size();j++) {
                    Ball2 ball2 = balls2.get(j); 
                    if (i<j) {
                        ball1.checkCollision(ball2);
                    }
                }
            } 
        }

        nave.update();

        for (Bullet b : balas) {
            b.update();
        }

        // vida extra
        if (vidaExtra.isVisible() && nave.getBounds().overlaps(vidaExtra.getBounds())) {
            nave.setVidas(nave.getVidas() + 1);
            vidaExtra.setVisible(false);
        }

        // escudo extra
        if (escudoExtra.isVisible() && nave.getBounds().overlaps(escudoExtra.getBounds())) {
            shieldActivo = true;
            shieldTimer = SHIELD_DURATION;
            escudoExtra.setVisible(false);
        }

        if (shieldActivo) {
            shieldTimer -= delta;
            if (shieldTimer <= 0) {
                shieldActivo = false;
            }
        }

        // colisiones nave-roca (handling shield logic, fix center and logic)
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            Rectangle naveRect = nave.getSprite().getBoundingRectangle();
            if (naveRect.overlaps(b.getArea())) {
                if (shieldActivo) {
                    b.daño(999); // sólo destruye la roca, no afecta la nave
                } else {
                    nave.checkCollision(b); // comportamiento normal de la nave y rebote
                }
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }
        }

        if (nave.isDestroyed()) {
            if (score > Settings.getInstance().getHighScore())
                Settings.getInstance().setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }

        if (balls1.size()==0) {
            int maxPelotas = 25;
            int nuevaCantidad = Math.min(cantAsteroides + 3, maxPelotas);
            int maxVelocidad = 8;
            int nuevaVelX = Math.min(velXAsteroides + 1, maxVelocidad);
            int nuevaVelY = Math.min(velYAsteroides + 1, maxVelocidad);

            Screen ss = new PantallaJuego(game, ronda+1, nave.getVidas(), score, 
                nuevaVelX, nuevaVelY, nuevaCantidad);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }
    
    @Override
    protected void onRender(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();

        for (Bullet b : balas) {
            b.draw(batch);
        }
        nave.draw(batch);
        for (Ball2 b : balls1) {
            b.draw(batch);
        }
        if (vidaExtra != null && vidaExtra.isVisible()) vidaExtra.draw(batch);
        if (escudoExtra != null && escudoExtra.isVisible()) escudoExtra.draw(batch);
        batch.end();

        // Círculo azul si el escudo está activo, centrado en la nave
        if (shieldActivo) {
            // --- DIBUJAR EL CÍRCULO SOLO USANDO LAS COORDENADAS DEL SPRITE DE LA NAVE ---
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 0.6f, 1f, 1);
            float x = nave.getSprite().getX();
            float y = nave.getSprite().getY();
            float w = nave.getSprite().getWidth();
            float h = nave.getSprite().getHeight();
            float centerX = x + w / 2f;
            float centerY = y + h / 2f;
            float radio = Math.max(w, h) / 2f + 12; // Ajusta el +12 a gusto
            shapeRenderer.circle(centerX, centerY, radio, 32);
            shapeRenderer.end();
        }
    }
    
    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }
    
    @Override
    protected void onShow() {
        if (gameMusic != null) gameMusic.play();
    }

    @Override
    protected void onResize(int width, int height) {}

    @Override
    protected void onDispose() {
        if (explosionSound != null) explosionSound.dispose();
        if (gameMusic != null) gameMusic.dispose();
        if (vidaExtra != null && vidaExtra.getSprite() != null) vidaExtra.getSprite().getTexture().dispose();
        if (escudoExtra != null && escudoExtra.getSprite() != null) escudoExtra.getSprite().getTexture().dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}