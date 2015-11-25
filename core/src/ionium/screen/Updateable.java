package ionium.screen;

import ionium.templates.Main;
import ionium.ui.UiContainer;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

public abstract class Updateable<T extends Main> implements Screen {

	public T main;
	public UiContainer container = new UiContainer();
	
	public Updateable(T m) {
		main = m;
	}

	@Override
	public abstract void render(float delta);

	/**
	 * updates once a render call only if this screen is active
	 */
	public abstract void renderUpdate();

	public abstract void tickUpdate();

	public abstract void getDebugStrings(Array<String> array);

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void show();

	@Override
	public abstract void hide();

	@Override
	public abstract void pause();

	@Override
	public abstract void resume();

	@Override
	public abstract void dispose();

	public void debug(String message) {
		Main.logger.debug(message);
	}

	public void debug(String message, Exception exception) {
		Main.logger.debug(message, exception);
	}

	public void info(String message) {
		Main.logger.info(message);
	}

	public void info(String message, Exception exception) {
		Main.logger.info(message, exception);
	}

	public void error(String message) {
		Main.logger.error(message);
	}

	public void error(String message, Throwable exception) {
		Main.logger.error(message, exception);
	}

	public void warn(String message) {
		Main.logger.warn(message);
	}

	public void warn(String message, Throwable exception) {
		Main.logger.warn(message, exception);
	}
	
	/**
	 * Called when scrolled. Negative amount means scrolled up.
	 * @param amount
	 */
	public boolean onScrolled(int amount){
		return false;
	}

}
