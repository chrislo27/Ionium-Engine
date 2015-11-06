package ionium.util.resolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;

import ionium.templates.Main;

/**
 * Helps determine resolution.
 * 
 *
 */
public class ResolutionDeterminator {

	/**
	 * 
	 * @param config
	 * @param possibleResolutions list of resolutions to use, will be sorted by size
	 */
	public static void determineIdealResolution(Resolutable config,
			Resolution[] possibleResolutions) {
		ArrayList<Resolution> sorted = new ArrayList<>();

		for (Resolution r : possibleResolutions) {
			sorted.add(r);
		}

		sorted.sort(new Comparator<Resolution>() {

			@Override
			public int compare(Resolution arg0, Resolution arg1) {
				return arg0.compareTo(arg1);
			}

		});

		for (int i = sorted.size() - 1; i >= 0; i--) {
			Resolution r = sorted.get(i);

			if (doesResolutionFit(r)) {
				config.setWidth(r.width);
				config.setHeight(r.height);
				break;
			}
		}
	}

	public static boolean doesResolutionFit(Resolution r) {

		if (r.width > Gdx.graphics.getDesktopDisplayMode().width
				|| r.height > Gdx.graphics.getDesktopDisplayMode().height) {
			return false;
		}

		return true;
	}

}
