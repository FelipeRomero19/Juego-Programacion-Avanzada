package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase que representa la vida extra (corazón).
 */
public class VidaExtra extends Entidad {

    private boolean visible = true;
    private int tamaño;

    public VidaExtra(float x, float y, int tamaño, Texture tx) {
        super(new Sprite(tx));
        this.setTamaño(tamaño);
        getSprite().setPosition(x, y);
        getSprite().setBounds(x, y, tamaño, tamaño);
    }

    @Override
    public void update() {
        // Sin movimiento
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (visible) super.draw(batch);
    }

    public Rectangle getBounds() {
        return getSprite().getBoundingRectangle();
    }

    public boolean isVisible() { 
    	return visible; 
    }
    public void setVisible(boolean v) { 
    	visible = v; 
    }

	public int getTamaño() {
		return tamaño;
	}

	public void setTamaño(int tamaño) {
		this.tamaño = tamaño;
	}
}
