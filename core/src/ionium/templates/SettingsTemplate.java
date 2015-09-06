package ionium.templates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsTemplate {

	private static SettingsTemplate instance;

	public static final int DEFAULT_WIDTH = 1920;
	public static final int DEFAULT_HEIGHT = 1080;

	public static boolean showFPS = true;
	public static boolean debug = false;
	public static float musicVolume = 1;
	public static float soundVolume = 1;

	public static int actualWidth = DEFAULT_WIDTH;
	public static int actualHeight = DEFAULT_HEIGHT;
	public static boolean fullscreen = false;

	public static final int TICKS = 20;
	public static final int TICKS_NANO = 1000000000 / TICKS;
	public static final int MAX_FPS = 60;

	static {
		SettingsTemplate.instance(); // init settings
	}

	private SettingsTemplate() {
	}

	public static SettingsTemplate instance() {
		if (instance == null) {
			instance = new SettingsTemplate();
			instance.loadResources();
		}
		return instance;
	}

	private Preferences pref;

	private void loadResources() {
		pref = getPref("settings");

		showFPS = pref.getBoolean("showFPS", true);
		soundVolume = pref.getFloat("soundVolume", 1f);
		musicVolume = pref.getFloat("musicVolume", 1f);
		actualWidth = pref.getInteger("actualWidth", DEFAULT_WIDTH);
		actualHeight = pref.getInteger("actualHeight", DEFAULT_HEIGHT);
	}

	public static Preferences getPref(String suffix) {
		return Gdx.app.getPreferences("" + "-" + suffix);
	}

	public void save() {
		pref.putBoolean("showFPS", SettingsTemplate.showFPS).putFloat("sound", soundVolume)
				.putFloat("music", musicVolume).putInteger("actualWidth", actualWidth)
				.putInteger("actualHeight", actualHeight).flush();
	}

	public static Preferences getPreferences() {
		return instance().pref;
	}

	private static final Resolution[] resolutions43 = new Resolution[] { new Resolution(640, 480),
			new Resolution(800, 600), new Resolution(1024, 768), new Resolution(1152, 864),
			new Resolution(1280, 960) };
	private static final Resolution[] resolutions169 = new Resolution[] {
			new Resolution(1176, 664), new Resolution(1280, 720), new Resolution(1360, 768),
			new Resolution(1366, 768), new Resolution(1600, 900), new Resolution(1768, 992),
			new Resolution(1920, 1080) };
	private static final Resolution[] resolutions1610 = new Resolution[] {
			new Resolution(1280, 800), new Resolution(1440, 900), new Resolution(1600, 1024),
			new Resolution(1680, 750) };

	public static Resolution[] get43ResolutionsList() {
		return resolutions43;
	}

	public static Resolution[] get169ResolutionsList() {
		return resolutions169;
	}

	public static Resolution[] get1610ResolutionsList() {
		return resolutions1610;
	}

	public static String[] get43ResolutionsStrings() {
		String[] list = new String[get43ResolutionsList().length];
		for (int i = 0; i < list.length; i++) {
			list[i] = get43ResolutionsList()[i].toString();
		}

		return list;
	}

	public static String[] get169ResolutionsStrings() {
		String[] list = new String[get169ResolutionsList().length];
		for (int i = 0; i < list.length; i++) {
			list[i] = get169ResolutionsList()[i].toString();
		}

		return list;
	}

	public static String[] get1610ResolutionsStrings() {
		String[] list = new String[get1610ResolutionsList().length];
		for (int i = 0; i < list.length; i++) {
			list[i] = get1610ResolutionsList()[i].toString();
		}

		return list;
	}

	public static class Resolution {

		public int width = 0;
		public int height = 0;

		public Resolution(int w, int h) {
			width = w;
			height = h;
		}

		@Override
		public String toString() {
			return width + "x" + height;
		}

	}

}
