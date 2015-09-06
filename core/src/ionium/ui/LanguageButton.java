package ionium.ui;

import ionium.registry.ConstantsRegistry;
import ionium.templates.Main;
import ionium.util.Translator;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;

public abstract class LanguageButton extends Button {

	public LanguageButton(UiCorner corner) {
		super(0, 0, 64f / ConstantsRegistry.getInt("DEFAULT_WIDTH"), 64f / ConstantsRegistry.getInt("DEFAULT_HEIGHT"), null);
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

	public abstract Preferences getPreferences();
	
	@Override
	public boolean onLeftClick() {
		Translator.instance().nextLang();
		getPreferences().putString("language", Translator.instance().currentLang()).flush();
		return true;
	}

	@Override
	public boolean onRightClick() {
		Translator.instance().prevLang();
		getPreferences().putString("language", Translator.instance().currentLang()).flush();
		return true;
	}
}
