package ionium.ui;

import ionium.registry.AssetRegistry;
import ionium.templates.Main;
import ionium.templates.SettingsTemplate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.MathUtils;

public class Slider extends UiElement {

	public float slider = 0;

	private boolean grabbed = false;

	public Slider(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		if (width == -1) this.width = 160;
		if (height == -1) this.height = 32;
	}

	public Slider setSlider(float f) {
		slider = f;
		return this;
	}

	@Override
	public void render(Main main) {
		main.batch.draw(AssetRegistry.getTexture("guislider"), x * Gdx.graphics.getWidth(), y * Gdx.graphics.getHeight(), width * Gdx.graphics.getWidth(),
				height * Gdx.graphics.getHeight());
		main.batch.draw(AssetRegistry.getTexture("guisliderarrow"), x * Gdx.graphics.getWidth()
				+ ((width * Gdx.graphics.getWidth() - 32) * slider), y * Gdx.graphics.getHeight(), 32, 32);

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (Gdx.input.getX() >= x * Gdx.graphics.getWidth() + ((width * Gdx.graphics.getWidth() - 32) * slider)
					&& Gdx.input.getX() <= x * Gdx.graphics.getWidth() + ((width * Gdx.graphics.getWidth() - 32) * slider) + 32) {
				if (Gdx.graphics.getHeight() - Gdx.input.getY() >= y * Gdx.graphics.getHeight()
						&& Gdx.graphics.getHeight() - Gdx.input.getY() <= y * Gdx.graphics.getHeight() + height * Gdx.graphics.getHeight()) {
					grabbed = true;
				}
			}
		} else {
			grabbed = false;
		}
		if (grabbed) {
			slider = MathUtils.clamp(((Gdx.input.getX() - x * Gdx.graphics.getWidth())) / (width * Gdx.graphics.getWidth() - 32f), 0f, 1f);
		}
	}

	@Override
	public boolean visible() {
		return true;
	}

	@Override
	public boolean onLeftClick() {
		return true;
	}

	@Override
	public boolean onRightClick() {
		return false;
	}

}
