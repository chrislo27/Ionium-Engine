package ionium.ui;

import ionium.registry.GlobalVariables;
import ionium.registry.ScreenRegistry;
import ionium.templates.Main;

public class SettingsButton extends Button {

	public SettingsButton(int x, int y, int width, int height) {
		super(0, 0, 1, 1, null);
		this.setFixedSize(x, y, width, height);
	}

	@Override
	public void render(Main main) {
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
