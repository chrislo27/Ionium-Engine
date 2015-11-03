package ionium.desktop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ArgumentInferredLwjglAppConfig extends LwjglApplicationConfiguration {

	private final String[] rawArgs;
	private HashMap<String, String> arguments = new HashMap<>();

	public ArgumentInferredLwjglAppConfig(String[] args) {
		super();

		this.rawArgs = args;
	}

	/**
	 * Turns rawArgs into the arguments HashMap and then infers
	 */
	public void inferFromArguments() {
		arguments.clear();

		if (rawArgs == null || rawArgs.length <= 0) return;

		for (int i = 0; i < rawArgs.length - (rawArgs.length % 2 == 1 ? -1 : 0); i += 2) {
			arguments.put(rawArgs[i], rawArgs[i + 1]);
		}

		Set<String> keys = arguments.keySet();
		Iterator<String> it = keys.iterator();

		String key;
		String value;
		while ((key = it.next()) != null) {
			value = arguments.get(key);

			checkKeyAndValue(key, value);
		}

	}

	private void checkKeyAndValue(String key, String value) {
		if (!key.startsWith("-")) return;

		value = value.toLowerCase();

		switch (key.toLowerCase()) {
		case "width":
			try {
				int i = Integer.parseInt(value);

				this.width = i;
			} catch (NumberFormatException ex) {

			}
			break;
		case "height":
			try {
				int i = Integer.parseInt(value);

				this.height = i;
			} catch (NumberFormatException ex) {

			}
			break;
		case "fullscreen":
			try {
				int i = Integer.parseInt(value);

				if (i == 0) {
					this.fullscreen = false;
				} else if (i == 1) {
					this.fullscreen = true;
				}
			} catch (NumberFormatException ex) {

			}
			break;
		case "vsync":
			try {
				int i = Integer.parseInt(value);

				if (i == 0) {
					this.vSyncEnabled = false;
				} else if (i == 1) {
					this.vSyncEnabled = true;
				}
			} catch (NumberFormatException ex) {

			}
			break;
		case "fpslock":
			try {
				int i = Integer.parseInt(value);

				this.foregroundFPS = i;
				this.backgroundFPS = i;
			} catch (NumberFormatException ex) {

			}
			break;
		case "msaasamples":
			try {
				int i = Integer.parseInt(value);

				this.samples = i;
			} catch (NumberFormatException ex) {

			}
			break;
		case "resizeable":
			try {
				int i = Integer.parseInt(value);

				if (i == 0) {
					this.resizable = false;
				} else if (i == 1) {
					this.resizable = true;
				}
			} catch (NumberFormatException ex) {

			}
			break;
		}
	}

}
