package ionium.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import ionium.animation.Animation;
import ionium.audio.Captioned;
import ionium.audio.CaptionedLoader;
import ionium.audio.CaptionedMusic;
import ionium.audio.CaptionedSound;
import ionium.registry.handler.IAssetLoader;
import ionium.registry.handler.StockAssetLoader;
import ionium.util.AssetMap;
import ionium.util.GameException;

public final class AssetRegistry implements Disposable {

	private static AssetRegistry instance;

	private AssetRegistry() {
	}

	public static AssetRegistry instance() {
		if (instance == null) {
			instance = new AssetRegistry();
			instance.onInstantiate();
		}
		return instance;
	}

	private Iterator<Entry<String, Animation>> animationLoadingIterator;

	private Array<IAssetLoader> loaders = new Array<>();

	private AssetManager manager = new AssetManager();
	private HashMap<String, Texture> unmanagedTextures = new HashMap<>();
	private HashMap<String, Animation> animations = new HashMap<>();

	private Texture missingTexture;

	private void onInstantiate() {
		manager.setLoader(Captioned.class, new CaptionedLoader(new InternalFileHandleResolver()));
		
		addAssetLoader(new StockAssetLoader());
	}

	public Array<IAssetLoader> getAllAssetLoaders() {
		return loaders;
	}

	public AssetManager getAssetManager() {
		return manager;
	}

	public HashMap<String, Texture> getUnmanagedTextures() {
		return unmanagedTextures;
	}

	public HashMap<String, Animation> getAnimations() {
		return animations;
	}

	public AssetRegistry addAssetLoader(IAssetLoader l) {
		loaders.add(l);

		// add the managed textures to the asset manager, the unmanaged textures are loaded separately
		l.addManagedAssets(manager);
		l.addUnmanagedAnimations(animations);
		
		return this;
	}

	/**
	 * calls the #update(int) method of the internal AssetManager and also loads an animation from the map if found.
	 * The amount of time the manager gets is half of the given time (bigger half) unless the animations are done loading. 
	 * The animations load for half the given time.
	 * <br>
	 * It is not guaranteed that the time this method blocks will be exactly the number of milliseconds given in the parameter.
	 * @param millis
	 */
	public synchronized void loadManagedAssets(int millis) {
		createAnimationLoadingIterator();

		int managerTimeShare = (animationLoadingIterator.hasNext() ? (millis / 2 + millis % 2)
				: millis);
		int animationTimeShare = (manager.getProgress() >= 1 ? millis : millis - managerTimeShare);

		if (manager.getProgress() < 1) manager.update(managerTimeShare);

		if (animationLoadingIterator.hasNext()) {
			long time = System.currentTimeMillis();
			while (true) {
				if(System.currentTimeMillis() - time >= animationTimeShare) break;
				if(!animationLoadingIterator.hasNext()) break;
				animationLoadingIterator.next().getValue().load();
			}
		}
	}

	public void loadUnmanagedTextures() {
		for (IAssetLoader l : loaders) {
			l.addUnmanagedTextures(unmanagedTextures);
		}
	}

	private void createAnimationLoadingIterator() {
		if (animationLoadingIterator == null) {
			animationLoadingIterator = animations.entrySet().iterator();
		}
	}

	public boolean finishedLoading() {
		createAnimationLoadingIterator();

		return (getAssetManager().getProgress() >= 1 && animationLoadingIterator.hasNext() == false);
	}

	@Override
	public void dispose() {
		manager.dispose();

		for (Entry<String, Texture> entry : unmanagedTextures.entrySet()) {
			entry.getValue().dispose();
		}

		for (Entry<String, Animation> entry : animations.entrySet()) {
			entry.getValue().dispose();
		}

		if (missingTexture != null) missingTexture.dispose();
	}

	public static Texture getMissingTexture() {
		if (instance().missingTexture == null) {
			throw new GameException(
					"Missing texture not created yet; forgot to call #createMissingTexture in "
							+ instance().getClass().getSimpleName());
		} else {
			return instance().missingTexture;
		}
	}

	public static void createMissingTexture() {
		if (instance().missingTexture != null) return;

		// generate missing texture
		Pixmap pix = new Pixmap(32, 32, Format.RGBA8888);

		// pink
		pix.setColor(1, 0, 1, 1);
		pix.fillRectangle(0, 0, pix.getWidth() / 2, pix.getHeight() / 2);
		pix.fillRectangle(pix.getWidth() / 2, pix.getHeight() / 2, pix.getWidth() / 2,
				pix.getHeight() / 2);

		// black
		pix.setColor(0, 0, 0, 1);
		pix.fillRectangle(pix.getWidth() / 2, 0, pix.getWidth() / 2, pix.getHeight() / 2);
		pix.fillRectangle(0, pix.getHeight() / 2, pix.getWidth() / 2, pix.getHeight() / 2);

		// set to texture
		instance().missingTexture = new Texture(pix);

		pix.dispose();
	}

	/**
	 * uses the instance() method and AssetMap key to return a Texture. It will attempt to search the unmanaged textures map first.
	 * @param key
	 * @return the Texture, searching unmanaged textures first or null if none is found
	 */
	public static Texture getTexture(String key) {
		if (instance().getUnmanagedTextures().get(key) != null) {
			return instance().getUnmanagedTextures().get(key);
		} else {
			if (AssetMap.get(key) == null) return getMissingTexture();

			return getAsset(key, Texture.class);
		}
	}

	public static Sound getSound(String key) {
		return getAsset(key, Sound.class);
	}

	public static Music getMusic(String key) {
		return getAsset(key, Music.class);
	}

	public static Animation getAnimation(String key) {
		return instance().getAnimations().get(key);
	}
	
	/**
	 * Gets an asset, putting the key through AssetMap.get first
	 * @param key
	 * @param clz
	 * @return
	 */
	public static <T> T getAsset(String key, Class<T> clz){
		return getAssetRaw(AssetMap.get(key), clz);
	}
	
	/**
	 * Gets an asset by the path provided
	 * @param path
	 * @param clz
	 * @return
	 */
	public static <T> T getAssetRaw(String path, Class<T> clz){
		return instance().getAssetManager().get(path, clz);
	}
	
	public static TiledMap getTiledMap(String key){
		return getAsset(key, TiledMap.class);
	}

}
