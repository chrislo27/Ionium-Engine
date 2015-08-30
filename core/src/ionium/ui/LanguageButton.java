package ionium.ui;

import ionium.templates.Main;
import ionium.templates.Settings;
import ionium.util.Translator;

import com.badlogic.gdx.graphics.Color;

public class LanguageButton extends Button {

	public LanguageButton(UiCorner corner) {
		super(0, 0, 64f / Settings.DEFAULT_WIDTH, 64f / Settings.DEFAULT_HEIGHT, null);
		this.setFixed(corner, 64, 64);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guilanguage");
		main.font.setColor(Color.WHITE);
		main.font.draw(
				main.batch,
				Translator.getMsg("menu.language") + ": "
						+ Translator.instance().languageList.get(Translator.instance().toUse), x
						+ width + 5, y + (height / 2));
	}

	@Override
	public boolean onLeftClick() {
		Translator.instance().nextLang();
		Settings.getPref("settings").putString("language", Translator.instance().currentLang()).flush();
		return true;
	}

	@Override
	public boolean onRightClick() {
		Translator.instance().prevLang();
		Settings.getPref("settings").putString("language", Translator.instance().currentLang()).flush();
		return true;
	}
}
