package ionium.ui;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import ionium.templates.Main;
import ionium.util.i18n.Localization;

public abstract class LanguageNextButton extends Button {

	public LanguageNextButton(int x, int y, int w, int h) {
		super(0, 0, 1, 1, null);
		this.setFixedSize(x, y, w, h);
	}

	@Override
	public abstract void render(Main main, BitmapFont font);

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
