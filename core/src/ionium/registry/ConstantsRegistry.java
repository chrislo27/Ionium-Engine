package ionium.registry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ConstantsRegistry {

	private static ConstantsRegistry instance;

	private ConstantsRegistry() {
	}

	public static ConstantsRegistry instance() {
		if (instance == null) {
			instance = new ConstantsRegistry();
			instance.loadResources();
		}
		return instance;
	}

	private Preferences settings = Gdx.app.getPreferences("GlobalGameConstants");

	private void loadResources() {
		loadDefaults();
	}

	private void loadDefaults() {
		settings.putInteger("DEFAULT_WIDTH", 1920);
		settings.putInteger("DEFAULT_HEIGHT", 1080);

		settings.putInteger("TICKS", 20);
		settings.putInteger("MAX_FPS", 60);
	}

	public static Preferences get() {
		return instance().settings;
	}

	public static int getInt(String key) {
		return get().getInteger(key);
	}

	public static boolean getBoolean(String key) {
		return get().getBoolean(key);
	}

	public static long getLong(String key) {
		return get().getLong(key);
	}

	public static String getString(String key) {
		return get().getString(key);
	}

	public static float getFloat(String key) {
		return get().getFloat(key);
	}
}
