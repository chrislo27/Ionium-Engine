package ionium.ui;

import ionium.templates.Main;
import ionium.util.Translator;

import com.badlogic.gdx.graphics.Color;

public class BooleanButton extends Button {

	public BooleanButton(int x, int y, int w, int h, String text) {
		super(x, y, w, h, text);
	}

	public boolean state = false;

	@Override
	public void render(Main main) {
		imageRender(main, "guibg" + state + "");
		main.font.setColor(Color.BLACK);
		renderText(main, Translator.getMsg(text), this.width);
	}

	@Override
	public boolean onLeftClick() {
		state = !state;
		return true;
	}

	@Override
	public boolean onRightClick() {
		state = !state;
		return true;
	}

	public BooleanButton setState(boolean b) {
		state = b;
		return this;
	}

}
