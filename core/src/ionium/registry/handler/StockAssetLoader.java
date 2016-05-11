package ionium.registry.handler;

import ionium.animation.Animation;
import ionium.animation.LoopingAnimation;
import ionium.util.AssetMap;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class StockAssetLoader implements IAssetLoader {

	@Override
	public void addManagedAssets(AssetManager manager) {
	}

	@Override
	public void addUnmanagedTextures(HashMap<String, Texture> textures) {
		// misc

		// unmanaged textures
		textures.put("gear", new Texture("images/gear.png"));

	}

	@Override
	public void addUnmanagedAnimations(HashMap<String, Animation> animations) {
		// animations
	}

}
