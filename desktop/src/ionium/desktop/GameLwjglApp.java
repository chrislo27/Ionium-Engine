package ionium.desktop;

import java.text.SimpleDateFormat;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ionium.application.GenericApplicationListener;
import ionium.util.Logger;

public class GameLwjglApp extends LwjglApplication {

	public GameLwjglApp(GenericApplicationListener listener, LwjglApplicationConfiguration config,
			Logger log) {
		super(listener, config);
		logger = log;

		genAppListener = listener;
	}

	private SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
	private Logger logger;

	private GenericApplicationListener genAppListener;

	public Logger getLogger() {
		return logger;
	}

	@Override
	public void debug(String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			logger.debug((tag != null ? tag : "") + message);
		}

	}

	@Override
	public void debug(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			logger.debug((tag != null ? tag : "") + message);
			exception.printStackTrace(System.out);
		}

	}

	@Override
	public void log(String tag, String message) {
		if (logLevel >= LOG_INFO) {
			logger.info((tag != null ? tag : "") + message);
		}

	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) {
			logger.info((tag != null ? tag : "") + message);
			exception.printStackTrace(System.out);
		}

	}

	@Override
	public void error(String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			logger.debug((tag != null ? tag : "") + message);
		}

	}

	@Override
	public void error(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) {
			logger.error((tag != null ? tag : "") + message, exception);
		}

	}

}
