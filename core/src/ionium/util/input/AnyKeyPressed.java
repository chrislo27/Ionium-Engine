package ionium.util.input;

import com.badlogic.gdx.Gdx;

public class AnyKeyPressed {

	public static boolean isAKeyJustPressed(int[] keys) {
		for (int i : keys) {
			if (Gdx.input.isKeyJustPressed(i)) return true;
		}

		return false;
	}

	public static boolean isAKeyPressed(int[] keys) {
		for (int i : keys) {
			if (Gdx.input.isKeyPressed(i)) return true;
		}

		return false;
	}

}
