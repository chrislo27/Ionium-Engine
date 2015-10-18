package ionium.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import ionium.templates.Main;

/**
 * same as ChoiceButton but it does not use Translator and does not have a starting message
 * 
 *
 */
public class ResolutionButton extends ChoiceButton {

	public ResolutionButton(int x, int y, int w, int h, String[] choices) {
		super(x, y, w, h, "", choices);
	}

	@Override
	public void render(Main main, BitmapFont font) {
		imageRender(main, "guibg");
		main.defaultFont.setColor(Color.BLACK);
		renderText(main, font, "< " + choices.get(selection) + " >", width);
	}

}
