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
		// ui
		manager.load(AssetMap.add("guilanguage", "images/ui/button/language.png"), Texture.class);
		manager.load(AssetMap.add("guisettings", "images/ui/button/settings.png"), Texture.class);
		manager.load(AssetMap.add("guibg", "images/ui/button/bg.png"), Texture.class);
		manager.load(AssetMap.add("guibgtext", "images/ui/button/bg-textfield.png"), Texture.class);
		manager.load(AssetMap.add("guislider", "images/ui/button/slider.png"), Texture.class);
		manager.load(AssetMap.add("guisliderarrow", "images/ui/button/sliderarrow.png"),
				Texture.class);
		manager.load(AssetMap.add("guiexit", "images/ui/button/exit.png"), Texture.class);
		manager.load(AssetMap.add("guiback", "images/ui/button/backbutton.png"), Texture.class);
		manager.load(AssetMap.add("guibgfalse", "images/ui/button/bgfalse.png"), Texture.class);
		manager.load(AssetMap.add("guibgtrue", "images/ui/button/bgtrue.png"), Texture.class);
		
		// sfx

		// music

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
