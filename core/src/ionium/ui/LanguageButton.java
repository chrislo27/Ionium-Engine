package ionium.ui;

import ionium.registry.GlobalVariables;
import ionium.templates.Main;
import ionium.util.i18n.Localization;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;

public abstract class LanguageButton extends Button {

	public LanguageButton(int x, int y, int w, int h) {
		super(0, 0, 1, 1, null);
		this.setFixedSize(x, y, w, h);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guilanguage");
		main.defaultFont.setColor(Color.WHITE);
		main.defaultFont.draw(
				main.batch,
				Localization.get("menu.language") + ": "
						+ Localization.instance().getCurrentBundle().getLocale().getName(), x
						+ width + 5, y + (height / 2));
	}

	public abstract Preferences getPreferences();
	
	@Override
	public boolean onLeftClick() {
		Localization.instance().nextLanguage(1);
		
		Localization.instance().saveToSettings(getPreferences());
		return true;
	}

	@Override
	public boolean onRightClick() {
		Localization.instance().nextLanguage(-1);
		
		Localization.instance().saveToSettings(getPreferences());
		return true;
	}
}
