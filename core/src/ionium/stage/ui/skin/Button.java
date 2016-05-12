package ionium.stage.ui.skin;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ionium.stage.Stage;
import ionium.stage.ui.AbstractButton;
import ionium.templates.Main;

/**
 * Button with a palette.
 * 
 *
 */
public class Button extends AbstractButton {

	public final Palette palette;

	public Button(Stage stage, Palette palette) {
		super(stage);

		this.palette = palette;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(palette.backgroundColor);
		Main.fillRect(batch, getX(), getY(), getWidth(), getHeight());

		batch.setColor(palette.borderColor);
		Main.drawRect(batch, getX(), getY(), getWidth(), getHeight(), palette.borderThickness);

		batch.setColor(1, 1, 1, 1);
	}

}
