package ionium.util.resolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;

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
	public void determineIdealResolution(Resolutable config, Resolution[] possibleResolutions) {
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
		
		for(Resolution r : sorted){
			if(doesResolutionFit(r)){
				
			}
		}
	}
	
	public static boolean doesResolutionFit(Resolution r){
		
		if(r.width >= Gdx.app.getGraphics().getWidth() || r.height >= Gdx.app.getGraphics().getHeight()){
			return false;
		}
		
		return true;
	}

}
