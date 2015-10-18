package ionium.ui;

import ionium.templates.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class UiElement {

	protected boolean fixedSize = false;
	protected int fixedWidth, fixedHeight, fixedX, fixedY;
	
	protected float x;
	protected float y;
	protected float width;
	protected float height;

	public abstract void render(Main main, BitmapFont font);

	public abstract boolean visible();

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public UiElement setFixedSize(int x, int y, int w, int h){
		fixedSize = true;
		fixedWidth = w;
		fixedHeight = h;
		fixedX = x;
		fixedY = y;
		
		return updateActualSizeFromFixed();
	}

	/**
	 * 
	 * @return true if handled
	 */
	public abstract boolean onLeftClick();

	/**
	 * 
	 * @return true if handled
	 */
	public abstract boolean onRightClick();

	public boolean onKeyTyped(char key) {
		return false;
	}
	
	public void onResize(){
		if(fixedSize) updateActualSizeFromFixed();
	}
	
	public UiElement updateActualSizeFromFixed(){
		width = fixedWidth * 1f / Gdx.graphics.getWidth();
		height = fixedHeight * 1f / Gdx.graphics.getHeight();
		x = fixedX * 1f / Gdx.graphics.getWidth();
		y = fixedY * 1f / Gdx.graphics.getHeight();
		
		return this;
	}
	
}
