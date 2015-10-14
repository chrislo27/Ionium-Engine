package ionium.ui;

import ionium.templates.Main;
import ionium.util.i18n.Localization;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class ChoiceButton extends Button {

	public ChoiceButton(int x, int y, int w, int h, String text, String[] choices) {
		super(x, y, w, h, text);
		this.choices.addAll(choices);
	}

	public Array<String> choices = new Array<String>();
	public int selection = 0;

	@Override
	public void render(Main main) {
		imageRender(main, "guibg");
		main.defaultFont.setColor(Color.BLACK);
		renderText(main, "< " + Localization.get(text) + Localization.get(choices.get(selection))
				+ " >", width);
	}

	@Override
	public boolean onLeftClick() {
		selection++;
		if (selection >= choices.size) {
			selection = 0;
		}
		return true;
	}

	@Override
	public boolean onRightClick() {
		selection--;
		if (selection < 0) {
			selection = choices.size - 1;
		}
		return true;
	}

}
