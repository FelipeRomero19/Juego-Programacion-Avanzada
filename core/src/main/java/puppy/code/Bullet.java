package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Bullet extends Entidad{

	private int xSpeed;
	private int ySpeed;
	private boolean destroyed = false;
	    
	    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
	    	super(new Sprite(tx));
	    	getSprite().setPosition(x, y);
	        this.xSpeed = xSpeed;
	        this.ySpeed = ySpeed;
	    }
	    
	    @Override
	    public void update() {
	    	if(destroyed) return;
	        getSprite().setPosition(getSprite().getX()+xSpeed, getSprite().getY()+ySpeed);
	        if (getSprite().getX() < 0 || getSprite().getX() + getSprite().getWidth() > Gdx.graphics.getWidth()) {
	            destroyed = true;
	        }
	        if (getSprite().getY() < 0 || getSprite().getY() + getSprite().getHeight() > Gdx.graphics.getHeight()) {
	        	destroyed = true;
	        }
	        
	    }
	    
	    @Override
	    public void draw(SpriteBatch batch) {
	    	if(!destroyed) super.draw(batch);
	    }
	    
	    /*
	     * comprueba colision con un dañable (ej: ball2).
	     * si colisiona devuelve true y marca la bala como destruida.
	     */
	    public boolean checkCollision(Dañable d) {
	    	if(destroyed) return false;
	        if(d instanceof Entidad){
	        	Entidad e = (Entidad) d;
	            if(getSprite().getBoundingRectangle().overlaps(e.getArea())) {
	            	this.destroyed = true;
	            	d.daño(1);
	            	return true;
	            }
	        }
	        return false;
	    }
	    
	    public boolean isDestroyed() {
	        return destroyed;
	    }
}
