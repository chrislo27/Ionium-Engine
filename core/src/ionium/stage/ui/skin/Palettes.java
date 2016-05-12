package ionium.stage.ui.skin;

/**
 * Holds a bunch of default skins.
 * 
 *
 */
public class Palettes {
	
	/**
	 * The original "Project MP" style of UI colours.
	 */
	public static Palette getIoniumDefault(){
		Palette p = new Palette();
		
		p.backgroundColor.set(229 / 255f, 229 / 255f, 229 / 255f, 1);
		p.borderColor.set(198 / 255f, 198 / 255f, 198 / 255f, 1);
		p.borderThickness = 4;
		
		return p;
	}

}
