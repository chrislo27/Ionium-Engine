package ionium.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import ionium.registry.GlobalVariables;
import ionium.registry.ScreenRegistry;
import ionium.templates.Main;
import ionium.ui.Button;

/**
 * Same as MessageScreen except it has a button to return to the main menu
 * 
 *
 */
public class ErrorScreen extends MessageScreen {

	public ErrorScreen(Main m, BitmapFont font) {
		super(m, font);

		container.elements.add(new Button((GlobalVariables.defaultWidth / 2) - 80, 128, 160, 32,
				"menu.backmainmenu") {

			@Override
			public boolean onLeftClick() {
				main.setScreen(ScreenRegistry.get("mainmenu"));
				return true;
			}

			@Override
			public boolean visible() {
				return true;
			}
		});
	}

}
