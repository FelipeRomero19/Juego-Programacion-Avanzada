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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;    
    private SpriteBatch batch;
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

    // --- Vida extra (corazón) ---
    private Texture vidaExtra;
    private float vidaExtraX, vidaExtraY;
    private boolean vidaExtraVisible = true;
    private int vidaExtraTamaño = 40;
    private Random rand = new Random();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        
        batch = game.getBatch();
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);
        //inicializar assets; musica de fondo y efectos de sonido
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1,0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); //
        
        gameMusic.setLooping(true);
        gameMusic.setVolume(game.getVolumen());
        gameMusic.play();
        
        // cargar imagen de la nave, 64x64   
        nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
                        Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                        new Texture(Gdx.files.internal("Rocket2.png")), 
                        Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        //crear asteroides
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            Ball2 bb = new Ball2(r.nextInt((int)Gdx.graphics.getWidth()),
                    50+r.nextInt((int)Gdx.graphics.getHeight()-50),
                    20+r.nextInt(10), velXAsteroides+r.nextInt(4), velYAsteroides+r.nextInt(4), 
                    new Texture(Gdx.files.internal("aGreyMedium4.png")));       
            balls1.add(bb);
            balls2.add(bb);
        }
        // --- Vida extra: cargar textura y posicionar aleatoriamente ---
        vidaExtra = new Texture(Gdx.files.internal("vida-Extra.png"));
        colocarVidaExtraAleatoria();
    }

    // Método para colocar el corazón en una posición aleatoria segura
    private void colocarVidaExtraAleatoria() {
        vidaExtraX = rand.nextInt(Gdx.graphics.getWidth() - vidaExtraTamaño);
        vidaExtraY = 100 + rand.nextInt(Gdx.graphics.getHeight() - vidaExtraTamaño - 100);
        vidaExtraVisible = true;
    }
    
    public void dibujaEncabezado() {
        CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
        game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();
        if (!nave.estaHerido()) {
            // colisiones entre balas y asteroides y su destruccion  
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {
                    Ball2 target = balls1.get(j);
                    if (b.checkCollision(target)) {
                        explosionSound.play(game.getVolumen());
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
        }
        //dibujar balas
        for (Bullet b : balas) {
            b.draw(batch);
        }
        nave.draw(batch, this);
        
        //dibujar asteroides y manejar colision con nave
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b=balls1.get(i);
            b.draw(batch);
            //perdió vida o game over
            if (nave.checkCollision(b)) {
                //asteroide se destruye con el choque             
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }      
        }

        // --- Dibuja el corazón si está visible ---
        if (vidaExtraVisible) {
            batch.draw(vidaExtra, vidaExtraX, vidaExtraY, vidaExtraTamaño, vidaExtraTamaño);
        }

        // --- Colisión nave con el corazón ---
        if (vidaExtraVisible && nave.getBounds().overlaps(
                new Rectangle(vidaExtraX, vidaExtraY, vidaExtraTamaño, vidaExtraTamaño))) {
            nave.setVidas(nave.getVidas() + 1); // Suma una vida
            vidaExtraVisible = false;
        }

        if (nave.isDestroyed()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
        batch.end();

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
    
    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }
    
    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        this.gameMusic.dispose();
        vidaExtra.dispose();
    }
}