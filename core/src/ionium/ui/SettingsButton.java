package ionium.ui;

import ionium.registry.GlobalVariables;
import ionium.registry.ScreenRegistry;
import ionium.templates.Main;

public class SettingsButton extends Button {

	public SettingsButton(UiCorner corner) {
		super(0, 0, 64f / GlobalVariables.getInt("DEFAULT_WIDTH"), 64f / GlobalVariables.getInt("DEFAULT_HEIGHT"), null);
		this.setFixed(corner, 64, 64);
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
