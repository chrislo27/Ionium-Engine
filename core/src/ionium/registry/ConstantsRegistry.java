package ionium.registry;

import java.util.HashMap;

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

	private HashMap<String, Integer> intMap = new HashMap<>();
	private HashMap<String, Boolean> booleanMap = new HashMap<>();
	private HashMap<String, Long> longMap = new HashMap<>();
	private HashMap<String, String> stringMap = new HashMap<>();
	private HashMap<String, Float> floatMap = new HashMap<>();

	private void loadResources() {
		loadDefaults();
	}

	private void loadDefaults() {
		putInt("DEFAULT_WIDTH", 1920);
		putInt("DEFAULT_HEIGHT", 1080);

		putInt("TICKS", 20);
		putInt("MAX_FPS", 60);
	}
	
	public ConstantsRegistry putInt(String key, int value){
		intMap.put(key, value);
		
		return this;
	}
	
	public ConstantsRegistry putBoolean(String key, boolean value){
		booleanMap.put(key, value);
		
		return this;
	}
	public ConstantsRegistry putLong(String key, long value){
		longMap.put(key, value);
		
		return this;
	}
	public ConstantsRegistry putString(String key, String value){
		stringMap.put(key, value);
		
		return this;
	}
	public ConstantsRegistry putFloat(String key, float value){
		floatMap.put(key, value);
		
		return this;
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public static long getLong(String key) {
		return getLong(key, 0L);
	}

	public static String getString(String key) {
		return getString(key, null);
	}

	public static float getFloat(String key) {
		return getFloat(key, 0f);
	}
	
	public static int getInt(String key, int def) {
		if (instance().intMap.get(key) == null) return def;

		return instance().intMap.get(key);
	}

	public static boolean getBoolean(String key, boolean def) {
		if (instance().booleanMap.get(key) == null) return def;

		return instance().booleanMap.get(key);
	}

	public static long getLong(String key, long def) {
		if (instance().longMap.get(key) == null) return def;

		return instance().longMap.get(key);
	}

	public static String getString(String key, String def) {
		if (instance().stringMap.get(key) == null) return def;

		return instance().stringMap.get(key);
	}

	public static float getFloat(String key, float def) {
		if (instance().floatMap.get(key) == null) return def;

		return instance().floatMap.get(key);
	}
}
