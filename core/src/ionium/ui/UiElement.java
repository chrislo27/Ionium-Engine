package ionium.ui;

import ionium.templates.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class UiElement {

	protected boolean fixedSize = false;
	protected float aspectRatio = 1;
	
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
	
	public UiElement setFixedSize(boolean b){
		fixedSize = b;
		
		aspectRatio = (width / height);
		
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
		width = height * aspectRatio;
		
		return this;
	}
	
}
