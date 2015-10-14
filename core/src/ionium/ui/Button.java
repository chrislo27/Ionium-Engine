package ionium.ui;

import ionium.registry.AssetRegistry;
import ionium.templates.Main;
import ionium.util.i18n.Translator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public class Button extends UiElement {

	public String text;
	protected GlyphLayout glyphLayout = new GlyphLayout();

	/**
	 * set width and/or height to -1 to set default
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 */
	public Button(float x, float y, float width, float height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}

	public void imageRender(Main main, String img) {
		main.batch.draw(AssetRegistry.getTexture(img), x * Gdx.graphics.getWidth(), y
				* Gdx.graphics.getHeight(), width * Gdx.graphics.getWidth(),
				height * Gdx.graphics.getHeight());
		if (UiContainer.mouseIn(this)) {
			main.batch.setColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 0.42f);
			main.batch.draw(AssetRegistry.getTexture(img), x * Gdx.graphics.getWidth(), y
					* Gdx.graphics.getHeight(), width * Gdx.graphics.getWidth(), height
					* Gdx.graphics.getHeight());
			main.batch.setColor(Color.WHITE);
		}
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guibg");
		renderText(main, Translator.getMsg(text), this.width * Gdx.graphics.getWidth());
	}

	protected void renderText(Main main, String text, float width) {
		main.font.setColor(Color.BLACK);

		glyphLayout.setText(main.font, text);
		if (glyphLayout.width + 6 > width) {
			//main.font.setScale(width / (main.font.getBounds(text).width + 6), 1);
		}

		main.font.draw(main.batch, text, x * Gdx.graphics.getWidth() + (width / 2), y
				* Gdx.graphics.getHeight() + (height * Gdx.graphics.getHeight() / 2)
				+ (glyphLayout.height / 2), 0, Align.center, false);
	}

	@Override
	public boolean onLeftClick() {
		return false;
	}

	@Override
	public boolean onRightClick() {
		return false;
	}

	@Override
	public boolean visible() {
		return true;
	}

}
