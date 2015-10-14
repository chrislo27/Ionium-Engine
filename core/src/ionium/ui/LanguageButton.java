package ionium.ui;

import ionium.registry.GlobalVariables;
import ionium.templates.Main;
import ionium.util.i18n.Localization;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;

public abstract class LanguageButton extends Button {

	public LanguageButton(UiCorner corner) {
		super(0, 0, 64f / GlobalVariables.getInt("DEFAULT_WIDTH"), 64f / GlobalVariables.getInt("DEFAULT_HEIGHT"), null);
		this.setFixed(corner, 64, 64);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guilanguage");
		main.font.setColor(Color.WHITE);
		main.font.draw(
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
