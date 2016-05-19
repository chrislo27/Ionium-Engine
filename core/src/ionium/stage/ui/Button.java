package ionium.stage.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ionium.stage.Actor;
import ionium.stage.Stage;
import ionium.stage.ui.skin.Palette;
import ionium.templates.Main;

/**
 * All the button functionality without the rendering.
 * 
 *
 */
public class Button extends Actor {

	private Palette palette;

	public Button(Stage stage, Palette palette) {
		super(stage);

		this.palette = palette;
	}

	public Button setPalette(Palette p) {
		palette = p;

		return this;
	}

	public Palette getPalette() {
		return palette;
	}

	@Override
	public void render(SpriteBatch batch, float alpha) {
		boolean isMouseOver = stage.isMouseOver(this);

		Color backgroundColor = palette.backgroundColor;
		if (!isEnabled()) {
			backgroundColor = palette.disabledBackgroundColor;
		} else if (isPressed()) {
			backgroundColor = palette.clickedBackgroundColor;
		} else if (isMouseOver) {
			backgroundColor = palette.mouseoverBackgroundColor;
		}

		batch.setColor(backgroundColor.r, backgroundColor.g, backgroundColor.b,
				backgroundColor.a * alpha);
		Main.fillRect(batch, getX(), getY(), getWidth(), getHeight());

		Color borderColor = palette.borderColor;
		if (!isEnabled()) {
			borderColor = palette.disabledBorderColor;
		} else if (isPressed()) {
			borderColor = palette.clickedBorderColor;
		} else if (isMouseOver) {
			borderColor = palette.mouseoverBorderColor;
		}

		batch.setColor(borderColor.r, borderColor.g, borderColor.b, borderColor.a * alpha);

		int borderThickness = palette.borderThickness;
		if (!isEnabled()) {
			borderThickness = palette.disabledBorderThickness;
		} else if (isPressed()) {
			borderThickness = palette.clickedBorderThickness;
		} else if (isMouseOver) {
			borderThickness = palette.mouseoverBorderThickness;
		}

		Main.drawRect(batch, getX(), getY(), getWidth(), getHeight(), borderThickness);

		batch.setColor(1, 1, 1, 1);
	}

}
