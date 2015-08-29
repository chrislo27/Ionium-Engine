package ionium.ui;

import ionium.templates.Main;

import com.badlogic.gdx.Gdx;

public abstract class UiElement {

	protected boolean fixedSize = false;
	protected int fixedWidth, fixedHeight;
	protected UiCorner fixedCorner = UiCorner.TOPLEFT;
	
	protected float x;
	protected float y;
	protected float width;
	protected float height;

	public abstract void render(Main main);

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
	
	public void setFixed(UiCorner corner, int w, int h){
		fixedSize = true;
		fixedWidth = w;
		fixedHeight = h;
		fixedCorner = corner;
		
		updateActualSizeFromFixed();
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
	
	protected void updateActualSizeFromFixed(){
		width = fixedWidth * 1f / Gdx.graphics.getWidth();
		height = fixedHeight * 1f / Gdx.graphics.getHeight();
		
		switch(fixedCorner){
		case BOTTOMLEFT:
			x = 0;
			y = 0;
			break;
		case BOTTOMRIGHT:
			x = 1f - width;
			y = 0;
			break;
		case TOPLEFT:
			x = 0;
			y = 1f - height;
			break;
		case TOPRIGHT:
			x = 1f - width;
			y = 1f - height;
			break;
		}
	}
	
	public static enum UiCorner{
		TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT;
	}
	
}
