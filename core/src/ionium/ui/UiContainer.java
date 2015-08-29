package ionium.ui;

import ionium.templates.Main;
import ionium.templates.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class UiContainer {

	public Array<UiElement> elements = new Array<UiElement>();

	public void render(Main main) {
		for (UiElement e : elements) {
			if (!e.visible()) continue;
			e.render(main);
		}
	}

	public boolean onLeftClick() {
		for (UiElement e : elements) {
			if (!e.visible()) continue;
			if (mouseIn(e)) if (e.onLeftClick()) return true;
		}

		return false;
	}

	public boolean onRightClick() {
		for (UiElement e : elements) {
			if (!e.visible()) continue;
			if (mouseIn(e)) if (e.onRightClick()) return true;
		}

		return false;
	}

	public boolean onKeyTyped(char c) {
		for (UiElement e : elements) {
			if (!e.visible()) continue;
			if (e.onKeyTyped(c)) return true;
		}

		return false;
	}

	protected static boolean mouseIn(UiElement e) {
		if (!e.visible()) return false;
		if (Gdx.input.getX() >= e.getX() * Gdx.graphics.getWidth()
				&& Gdx.input.getX() <= (e.getX() * Gdx.graphics.getWidth())
						+ (e.getWidth() * Gdx.graphics.getWidth())) {
			if (Gdx.graphics.getHeight() - Gdx.input.getY() >= e.getY() * Gdx.graphics.getHeight()
					&& Gdx.graphics.getHeight() - Gdx.input.getY() <= (e.getY() * Gdx.graphics
							.getHeight()) + (e.getHeight() * Gdx.graphics.getHeight())) {
				return true;
			}
		}

		return false;
	}
	
	public void onResize(){
		for (UiElement e : elements) {
			e.onResize();
		}
	}
}
