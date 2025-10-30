package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;



public class Nave4 extends Entidad implements Dañable {
	
	private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	super(new Sprite(tx));
    	this.sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	getSprite().setPosition(x, y);
    	getSprite().setBounds(x, y, 45, 45);

    }
    
    @Override
    public void update() {
    	// logica en draw
    }
    
    public void draw(SpriteBatch batch, PantallaJuego juego){
        float x =  getSprite().getX();
        float y =  getSprite().getY();
        if (!herido) {
	        // que se mueva con teclado
	        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xVel--;
	        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xVel++;
        	if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) yVel--;     
	        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) yVel++;

	        // que se mantenga dentro de los bordes de la ventana
	        if (x+xVel < 0 || x+xVel+getSprite().getWidth() > Gdx.graphics.getWidth())
	        	xVel*=-1;
	        if (y+yVel < 0 || y+yVel+getSprite().getHeight() > Gdx.graphics.getHeight())
	        	yVel*=-1;
	        
	        getSprite().setPosition(x+xVel, y+yVel);   
         
 		    getSprite().draw(batch);
        } 
        else {
           getSprite().setX(getSprite().getX()+MathUtils.random(-2,2));
 		   getSprite().draw(batch); 
 		   getSprite().setX(x);
 		   tiempoHerido--;
 		   if (tiempoHerido<=0) herido = false;
 		 }
        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {         
          Bullet  bala = new Bullet(getSprite().getX()+getSprite().getWidth()/2-5,
        		  getSprite().getY()+ getSprite().getHeight()-5,0,3,txBala);
	      juego.agregarBala(bala);
	      soundBala.play();
        }
       
    }
      
    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(getSprite().getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());

            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
    // implementacion de daño
    @Override
    public void daño(int cantidad) {
    	// reducimos vidas y marcamos herido/destruido si procede
    	this.vidas -= cantidad;
    	this.herido = true;
    	this.tiempoHerido = this.tiempoHeridoMax;
    	sonidoHerido.play();
    	if(this.vidas <= 0) this.destruida = true;
    }
    
    @Override
    public boolean isDestroyed() {
    	return destruida && !herido;
    }
    
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getVidas() {
    	return vidas;
    }

	public void setVidas(int vidas2) {
		vidas = vidas2;
	}
}
