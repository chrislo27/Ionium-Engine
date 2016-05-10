package ionium.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public abstract class Actor {

	private Rectangle screenOffset = new Rectangle();
	private Rectangle pixelOffset = new Rectangle();
	private Rectangle actualPosition = new Rectangle();

	protected final Stage stage;

	private int align = Align.center;
	private Rectangle viewport = new Rectangle();

	public Actor(Stage s) {
		stage = s;
		viewport.set(s.getCamera().position.x, s.getCamera().position.y,
				s.getCamera().viewportWidth, s.getCamera().viewportHeight);
	}

	public abstract void render(SpriteBatch batch);

	public void updateActualPosition() {
		float x = viewport.x;
		float y = viewport.y;
		float viewportWidth = viewport.width;
		float viewportHeight = viewport.height;

		float originX = viewportWidth * screenOffset.x + pixelOffset.x;
		float originY = viewportHeight * screenOffset.y + pixelOffset.y;
		float originWidth = viewportWidth * screenOffset.width + pixelOffset.width;
		float originHeight = viewportHeight * screenOffset.height + pixelOffset.height;

		if ((align & Align.right) == Align.right) {
			actualPosition.x = viewportWidth - originX - originWidth + x;
		} else if ((align & Align.center) == Align.center) {
			actualPosition.x = viewportWidth * 0.5f - originWidth * 0.5f + originX + x;
		} else {
			actualPosition.x = originX + x;
		}

		if ((align & Align.top) == Align.top) {
			actualPosition.y = viewportHeight - originY - originHeight + y;
		} else if ((align & Align.center) == Align.center) {
			actualPosition.y = viewportHeight * 0.5f - originHeight * 0.5f + originY + y;
		} else {
			actualPosition.y = originY + y;
		}

		actualPosition.setSize(originWidth, originHeight);
	}

	public Rectangle getViewport() {
		return viewport;
	}

	public Actor setViewportToStageCamera() {
		viewport.set(stage.getCamera().position.x - stage.getCamera().viewportWidth * 0.5f,
				stage.getCamera().position.y - stage.getCamera().viewportHeight * 0.5f,
				stage.getCamera().viewportWidth, stage.getCamera().viewportHeight);

		return this;
	}

	public Actor align(int align) {
		this.align = align;
		updateActualPosition();

		return this;
	}

	public int getAlign() {
		return align;
	}

	public float getScreenOffsetX() {
		return screenOffset.x;
	}

	public float getScreenOffsetY() {
		return screenOffset.y;
	}

	public float getScreenOffsetWidth() {
		return screenOffset.width;
	}

	public float getScreenOffsetHeight() {
		return screenOffset.height;
	}

	public float getPixelOffsetX() {
		return pixelOffset.x;
	}

	public float getPixelOffsetY() {
		return pixelOffset.y;
	}

	public float getPixelOffsetWidth() {
		return pixelOffset.width;
	}

	public float getPixelOffsetHeight() {
		return pixelOffset.height;
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
		setViewportToStageCamera();
		updateActualPosition();
	}

}
