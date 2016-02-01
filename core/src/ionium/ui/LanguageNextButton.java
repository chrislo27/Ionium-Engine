package ionium.ui;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import ionium.templates.Main;
import ionium.util.i18n.Localization;

public abstract class LanguageNextButton extends Button {

	public LanguageNextButton(float x, float y, float w, float h) {
		super(x, y, w, h, null);
		this.setFixedSize(true);
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
