package ionium.stage.ui.skin;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Holds a bunch of default skins.
 * 
 *
 */
public class Palettes {

	/**
	 * The original "Project MP" style of UI colours.
	 */
	public static Palette getIoniumDefault(BitmapFont font) {
		Palette p = new Palette();

		p.setBackgroundColor(229 / 255f, 229 / 255f, 229 / 255f, 1);
		p.mouseoverBackgroundColor.add(0.025f, 0.025f, 0.025f, 0);
		p.clickedBackgroundColor.add(0, 0.75f, 0.75f, 0);

		p.setBorderColor(198 / 255f, 198 / 255f, 198 / 255f, 1);
		p.mouseoverBorderColor.add(0.025f, 0.025f, 0.025f, 0);
		p.clickedBorderColor.add(0, 0.75f, 0.75f, 0);

		p.setTextColor(0, 0, 0, 1);

		p.setBorderThickness(4);

		p.textFont = font;

		return p;
	}

}
