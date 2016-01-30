package ionium.runnables;

import com.badlogic.gdx.audio.Music;

public class AudioPitchChange implements Runnable {

	public final Music mus;
	public final float pitch;
	
	public AudioPitchChange(Music mus, float pitch) {
		this.mus = mus;
		this.pitch = pitch;
	}

	@Override
	public void run() {
	}

}
