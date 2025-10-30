package puppy.code;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/*
 * Clase abstracta para entidades del juego que tienen un sprite y pueden
 * actualizarse/dibujarse -> encapsulamiento y reutilizacion
*/

public abstract class Entidad {
	
	protected Sprite spr;
	
	public Entidad(Sprite spr) {
		this.spr = spr;
	}
	
	public Sprite getSprite() {
		return spr;
	}
	
	public void setSprite(Sprite spr) {
		this.spr = spr;
	}
	
	public float getX() {
		return spr.getX();
	}
	
	public float getY() {
		return spr.getY();
	}
	
	public void setPosition(float x, float y) {
		spr.setPosition(x, y);
	}
	
	public Rectangle getArea() {
		return spr.getBoundingRectangle();
	}

	// Actualiza la posicion o estado de la entidad
	public abstract void update();
	
	// Dibuja la entidad en pantalla
	public void draw(SpriteBatch batch) {
		spr.draw(batch);
	}
}
