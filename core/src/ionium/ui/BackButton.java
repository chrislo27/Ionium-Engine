package ionium.ui;

import ionium.templates.Main;
import ionium.templates.Settings;

public abstract class BackButton extends Button {

	public BackButton(UiCorner corner) {
		super(0, 0, 64f / Settings.DEFAULT_WIDTH, 64f / Settings.DEFAULT_HEIGHT, null);
		this.setFixed(corner, 64, 64);
	}

	@Override
	public void render(Main main) {
		imageRender(main, (usesExitTex ? exitpath : backpath));
	}

	private static final String exitpath = "guiexit";
	private static final String backpath = "guiback";

	private boolean usesExitTex = false;

	public BackButton useExitTexture() {
		usesExitTex = true;
		return this;
	}

	@Override
	public abstract boolean onLeftClick();
}
