package ionium.util.i18n;

import ionium.templates.Main;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

public class Localization {

	private static Localization instance;

	private Localization() {
	}

	public static Localization instance() {
		if (instance == null) {
			instance = new Localization();
			instance.loadResources();
		}
		return instance;
	}

	public String getCurrentLanguageName() {
		return languageList.get(toUse);
	}

	public void setLanguage(String lang) {
		for (int i = 0; i < languageList.size; i++) {
			if (languageList.get(i).equalsIgnoreCase(lang)) {
				toUse = i;
				return;
			}
		}
		toUse = 0;
	}

	public String nextLang() {
		if (bundles.size() == 1) return languageList.get(toUse);
		if (toUse + 1 == bundles.size()) {
			toUse = 0;
		} else {
			toUse++;
		}
		return languageList.get(toUse);
	}

	public String prevLang() {
		if (bundles.size() == 1) return languageList.get(toUse);
		if (toUse - 1 < 0) {
			toUse = bundles.size() - 1;
		} else {
			toUse--;
		}
		return languageList.get(toUse);
	}

	private Array<String> caught = new Array<String>(128);
	private FileHandle base;

	public HashMap<String, I18NBundle> bundles = new HashMap<String, I18NBundle>();

	public int toUse = 0;
	public Array<String> languageList = new Array<String>();

	public static final String defaultLang = "English";

	private void loadResources() {
		base = Gdx.files.internal("localization/default");

		languageList.ordered = true;

		languageList.clear();

		addBundle(defaultLang, I18NBundle.createBundle(base, new Locale("")));

	}

	public void reloadFromFile() {
		for (int i = 0; i < languageList.size; i++) {
			Locale locale = bundles.get(languageList.get(i)).getLocale();

			bundles.put(languageList.get(i), I18NBundle.createBundle(base, locale));
		}
	}

	public void loadFromSettings(Preferences settings) {
		for (int i = 0; i < languageList.size; i++) {
			String lang = languageList.get(i);
			if (lang.equalsIgnoreCase(settings.getString("language", defaultLang))) {
				toUse = i;
			}
		}
	}

	private void addBundle(String name, I18NBundle bundle) {
		languageList.add(name);
		bundles.put(name, bundle);
	}

	private static I18NBundle getBundle() {
		return instance().bundles.get(instance().languageList.get(instance().toUse));
	}

	public I18NBundle getCurrentBundle() {
		return bundles.get(languageList.get(toUse));
	}

	public Locale getCurrentLocale() {
		return getCurrentBundle().getLocale();
	}

	public static String getMsg(String key, Object... params) {
		String s = "";

		if (instance().caught.contains(key, false)) {
			return key;
		}

		try {
			if (params == null) {
				s = getBundle().get(key);
			} else s = getBundle().format(key, params);
		} catch (MissingResourceException m) {
			if (!instance().caught.contains(key, false)) {
				instance().caught.add(key + "");
				Main.logger.warn("WARNING: the bundle \"" + instance().base.nameWithoutExtension()
						+ "_" + getBundle().getLocale().toString() + "\" has no key \"" + key
						+ "\"");
			}
			return key;
		}
		return s;
	}

}
