package ionium.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

public abstract class Actor {

	private Rectangle screenOffset = new Rectangle();
	private Rectangle pixelOffset = new Rectangle();
	private Rectangle actualPosition = new Rectangle();

	protected final Stage stage;

	private int align = Align.center;

	public Actor(Stage s) {
		stage = s;
	}

	public abstract void render(SpriteBatch batch);

	public void updateActualPosition() {
		float originX = stage.getCamera().viewportWidth * screenOffset.x + pixelOffset.x;
		float originY = stage.getCamera().viewportHeight * screenOffset.y + pixelOffset.y;
		float originWidth = stage.getCamera().viewportWidth * screenOffset.width
				+ pixelOffset.width;
		float originHeight = stage.getCamera().viewportHeight * screenOffset.height
				+ pixelOffset.height;

		if ((align & Align.right) == Align.right) {
			actualPosition.x = stage.getCamera().viewportWidth - originX - originWidth;
		} else if ((align & Align.center) == Align.center) {
			actualPosition.x = stage.getCamera().viewportWidth * 0.5f - originWidth * 0.5f
					+ originX;
		} else {
			actualPosition.x = originX;
		}

		if ((align & Align.top) == Align.top) {
			actualPosition.y = stage.getCamera().viewportHeight - originY - originHeight;
		} else if ((align & Align.center) == Align.center) {
			actualPosition.y = stage.getCamera().viewportHeight * 0.5f - originHeight * 0.5f
					+ originY;
		} else {
			actualPosition.y = originY;
		}

		actualPosition.setSize(originWidth, originHeight);
	}

	public Actor align(int align) {
		this.align = align;
		updateActualPosition();

		return this;
	}

	public int getAlign() {
		return align;
	}

	public Actor setScreenOffset(float x, float y, float w, float h) {
		screenOffset.set(x, y, w, h);
		updateActualPosition();

		return this;
	}

	public Actor setScreenOffset(float x, float y) {
		return setScreenOffset(x, y, screenOffset.width, screenOffset.height);
	}

	public Actor setScreenOffsetSize(float w, float h) {
		return setScreenOffset(screenOffset.x, screenOffset.y, w, h);
	}

	public Actor setPixelOffset(float x, float y, float w, float h) {
		pixelOffset.set(x, y, w, h);
		updateActualPosition();

		return this;
	}

	public Actor setPixelOffset(float x, float y) {
		return setPixelOffset(x, y, pixelOffset.width, pixelOffset.height);
	}

	public Actor setPixelOffsetSize(float w, float h) {
		return setPixelOffset(pixelOffset.x, pixelOffset.y, w, h);
	}

	public float actualX() {
		return actualPosition.x;
	}

	public float actualY() {
		return actualPosition.y;
	}

	public float actualWidth() {
		return actualPosition.width;
	}

	public float actualHeight() {
		return actualPosition.height;
	}

	public void onResize(int width, int height) {
		updateActualPosition();
	}

}
