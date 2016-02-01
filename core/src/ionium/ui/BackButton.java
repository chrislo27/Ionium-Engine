package ionium.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import ionium.templates.Main;

public abstract class BackButton extends Button {

	public BackButton(float x, float y, float w, float h) {
		super(x, y, w, h, null);
		this.setFixedSize(true);
	}

	@Override
	public void render(Main main, BitmapFont font) {
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
