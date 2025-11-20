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
        //inicializar assets; musica de fondo y efectos de sonido
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1,0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); //
        
        gameMusic.setLooping(true);
        gameMusic.setVolume(Settings.getInstance().getVolumen());
        gameMusic.play();
        
        // factory por defecto
        factory = new DefaultEntityFactory();
        
        // crear nave con factory (le pasamos "this" para que pueda agregar balas)
        nave = factory.createNave(Gdx.graphics.getWidth()/2-50,30,
                new Texture(Gdx.files.internal("MainShip3.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                new Texture(Gdx.files.internal("Rocket2.png")), 
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")), this);
        nave.setVidas(vidas);

        // crear asteroides con factory
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
    }

    // Método para colocar el corazón en una posición aleatoria segura
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
        // actualizar lógica del juego
        if (!nave.estaHerido()) {
            // colisiones entre balas y asteroides y su destruccion  
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {
                    Ball2 target = balls1.get(j);
                    if (b.checkCollision(target)) {
                        explosionSound.play(Settings.getInstance().getVolumen());
                        // si el asteroide quedo marcado como destruido, lo eliminamos
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
                    i--; //para no saltarse 1 tras eliminar del arraylist
                }
            }
            //actualizar movimiento de asteroides dentro del area
            for (Ball2 ball : balls1) {
                ball.update();
            }
            //colisiones entre asteroides y sus rebotes  
            for (int i=0;i<balls1.size();i++) {
                Ball2 ball1 = balls1.get(i);   
                for (int j=0;j<balls2.size();j++) {
                    Ball2 ball2 = balls2.get(j); 
                    if (i<j) {
                        ball1.checkCollision(ball2);
                    }
                }
            } 
        } else {
            // si la nave está herida igual actualizamos balas (ya se hace arriba)
        }

        // Actualiza nave (aquí se maneja input y disparo, separado del render)
        nave.update();

        // actualiza balas (si quedan)
        for (Bullet b : balas) {
            // ya actualizado arriba, pero aseguramos que se actualizan si nave está herido
            b.update();
        }

        //colision vida extra con nave
        if (vidaExtra.isVisible() && nave.getBounds().overlaps(vidaExtra.getBounds())) {
            nave.setVidas(nave.getVidas() + 1); // Suma una vida
            vidaExtra.setVisible(false);
        }

        // eliminar asteroides que colisionan con nave
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b=balls1.get(i);
            if (nave.checkCollision(b)) {
                //asteroide se destruye con el choque             
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

        //nivel completado
        if (balls1.size()==0) {
            // Limita el aumento de pelotas y velocidad por ronda
            int maxPelotas = 25; // Cambia este valor según lo que te parezca jugable
            int nuevaCantidad = Math.min(cantAsteroides + 3, maxPelotas); // Solo suma 3 por ronda, nunca más de 25

            int maxVelocidad = 8; // Velocidad máxima permitida
            int nuevaVelX = Math.min(velXAsteroides + 1, maxVelocidad); // Solo suma 1, nunca más de 8
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

        //dibujar balas
        for (Bullet b : balas) {
            b.draw(batch);
        }

        // dibujar nave
        nave.draw(batch);
        
        //dibujar asteroides y manejar colision con nave
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b=balls1.get(i);
            b.draw(batch);
        }

        // dibujar vida extra si visible
        if (vidaExtra != null && vidaExtra.isVisible()) {
            vidaExtra.draw(batch);
        }

        batch.end();
    }
    
    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }
    
    @Override
    protected void onShow() {
        if (gameMusic != null) gameMusic.play();
    }

    @Override
    protected void onResize(int width, int height) {
    }

    @Override
    protected void onDispose() {
        if (explosionSound != null) explosionSound.dispose();
        if (gameMusic != null) gameMusic.dispose();
        if (vidaExtra != null && vidaExtra.getSprite() != null) vidaExtra.getSprite().getTexture().dispose();
        // También eliminar texturas de asteroides, balas, nave si son creadas aquí.
    }
}
