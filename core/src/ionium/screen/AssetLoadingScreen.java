package ionium.screen;

import ionium.registry.AssetRegistry;
import ionium.registry.ScreenRegistry;
import ionium.templates.Main;
import ionium.templates.Settings;
import ionium.util.AssetLogger;
import ionium.util.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;

public class AssetLoadingScreen extends MiscLoadingScreen {

	public AssetLoadingScreen(Main m) {
		super(m);
		AssetRegistry.instance().getAssetManager().setLogger(output);
	}

	private AssetLogger output = new AssetLogger("assetoutput", Logger.DEBUG);

	private long startms = 0;

	private boolean waitedAFrame = false;

	@Override
	public void render(float delta) {
		AssetManager manager = AssetRegistry.instance().getAssetManager();
		
		AssetRegistry.instance().loadManagedAssets(((int) (1000f / Settings.MAX_FPS)));
		do {
			if (AssetRegistry.instance().finishedLoading()) {
				if (!waitedAFrame) {
					waitedAFrame = true;
					break;
				}

				Main.logger.info("Finished loading all managed assets, took "
						+ (System.currentTimeMillis() - startms) + " ms");

				main.setScreen(ScreenRegistry.get("mainmenu"));
			}
		} while (false);

		super.render(delta);

		main.batch.begin();
		main.batch.setColor(1, 1, 1, 1);
		Main.fillRect(main.batch, Gdx.graphics.getWidth() / 2 - 128, Gdx.graphics.getHeight() / 2 - 10,
				256 * manager.getProgress(), 20);

		Main.fillRect(main.batch, Gdx.graphics.getWidth() / 2 - 130, Gdx.graphics.getHeight() / 2 - 12, 260, 1);
		Main.fillRect(main.batch, Gdx.graphics.getWidth() / 2 - 130, Gdx.graphics.getHeight() / 2 + 11, 260, 1);

		Main.fillRect(main.batch, Gdx.graphics.getWidth() / 2 - 130, Gdx.graphics.getHeight() / 2 - 12, 1, 24);
		Main.fillRect(main.batch, Gdx.graphics.getWidth() / 2 + 132, Gdx.graphics.getHeight() / 2 - 12, 1, 24);

		if (manager.getAssetNames().size > 0) {
			main.drawTextBg(main.font, output.getLastMsg(),
					Gdx.graphics.getWidth() / 2
							- (Utils.getWidth(main.font, output.getLastMsg()) / 2),
					Gdx.graphics.getHeight() / 2 - 35);
		}
		String percent = String.format("%.0f", (manager.getProgress() * 100f)) + "%";
		main.drawTextBg(main.font, percent,
				Gdx.graphics.getWidth() / 2 - (Utils.getWidth(main.font, percent) / 2),
				Gdx.graphics.getHeight() / 2 - 60);

		main.batch.end();
	}

	@Override
	public void tickUpdate() {

	}

	@Override
	public void show() {
		startms = System.currentTimeMillis();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {

	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void renderUpdate() {
	}

}
