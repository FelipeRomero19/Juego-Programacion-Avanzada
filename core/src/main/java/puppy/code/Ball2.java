package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Ball2 extends Entidad implements Dañable {

    private int xSpeed;
    private int ySpeed;
    private boolean destroyed = false;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
    	super(new Sprite(tx));
    	Sprite spr = getSprite();
 	
        // matener dentro de pantalla
    	int px = x;
    	if (x-size < 0) px = x+size;
    	if (x+size > Gdx.graphics.getWidth()) px = x-size;
         
        int py = y; 
    	if (y-size < 0) py = y+size;
    	if (y+size > Gdx.graphics.getHeight()) py = y-size;
    	
        spr.setPosition(px, py);
        spr.setBounds(px, py, size * 2, size * 2);
        
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
    }
    
    @Override
    public void update() {
    	if(destroyed) return;
    	Sprite spr = getSprite();
        float x = spr.getX() + getXSpeed();
        float y = spr.getY() + getySpeed();

        if (x < 0 || x+spr.getWidth() > Gdx.graphics.getWidth())
        	setXSpeed(getXSpeed() * -1);
        if (y < 0 || y+spr.getHeight() > Gdx.graphics.getHeight())
        	setySpeed(getySpeed() * -1);
        spr.setPosition(spr.getX() + getXSpeed(), spr.getY() + getySpeed());
    }

    @Override
    public Rectangle getArea() {
    	return super.getArea();
    }
    
    @Override
    public void draw(SpriteBatch batch) {
    	if(!destroyed) super.draw(batch);
    }
    
    public void checkCollision(Ball2 b2) {
    	if(destroyed || b2.destroyed) return;
        if(getArea().overlaps(b2.getArea())){
        	// rebote simple conservando momentum aproximado
            if (getXSpeed() ==0) setXSpeed(getXSpeed() + b2.getXSpeed()/2);
            if (b2.getXSpeed() ==0) b2.setXSpeed(b2.getXSpeed() + getXSpeed()/2);
        	setXSpeed(- getXSpeed());
            b2.setXSpeed(-b2.getXSpeed());
            
            if (getySpeed() ==0) setySpeed(getySpeed() + b2.getySpeed()/2);
            if (b2.getySpeed() ==0) b2.setySpeed(b2.getySpeed() + getySpeed()/2);
            setySpeed(- getySpeed());
            b2.setySpeed(- b2.getySpeed()); 
        }
    }
    
    
    @Override
    public boolean isDestroyed() {
    	return destroyed;
    }
    
    // implementacion de daño
    @Override
    public void daño(int cantidad) {
    	// un solo golpe destruye al asteroide
    	this.destroyed = true;
    }
    
	public int getXSpeed() {
		return xSpeed;
	}
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	public int getySpeed() {
		return ySpeed;
	}
	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
    
}
