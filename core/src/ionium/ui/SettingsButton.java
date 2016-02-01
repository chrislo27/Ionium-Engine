package ionium.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import ionium.registry.ScreenRegistry;
import ionium.templates.Main;

public class SettingsButton extends Button {

	public SettingsButton(float x, float y, float w, float h) {
		super(x, y, w, h, null);
		this.setFixedSize(true);
	}

	@Override
	public void render(Main main, BitmapFont font) {
		imageRender(main, "guisettings");
		if (this.main == null) this.main = main;
	}

	private Main main = null;

	@Override
	public boolean onLeftClick() {
		if (main == null) {
			return false;
		}
		main.setScreen(ScreenRegistry.get("settings"));
		return true;
	}

}
