package ionium.screen;

import ionium.registry.ScreenRegistry;
import ionium.templates.Main;
import ionium.transition.TrainDoors;
import ionium.ui.BackButton;
import ionium.ui.Button;
import ionium.ui.SettingsButton;
import ionium.ui.UiElement.UiCorner;
import ionium.util.Translator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;

public strictfp class MainMenuScreen extends Updateable {

	public MainMenuScreen(Main m) {
		super(m);

		container.elements.add(new Button(0.25f, 0.1f, 0.5f, 0.1f,
				"menu.story") {

			@Override
			public boolean onLeftClick() {
				main.transition(new TrainDoors(true), new TrainDoors(false), ScreenRegistry.get("scenario"));
				
				return true;
			}
		});
		container.elements.add(new Button(0.25f, 0.25f, 0.5f, 0.1f,
				"something here") {

			@Override
			public boolean onLeftClick() {
				return false;
			}
			
		});
		container.elements.add(new SettingsButton(UiCorner.BOTTOMLEFT));
		container.elements.add(new BackButton(UiCorner.TOPRIGHT) {

			@Override
			public boolean onLeftClick() {
				Gdx.app.exit();
				System.exit(0);
				return true;
			}
		}.useExitTexture());
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();

		main.font.setColor(Color.WHITE);
		main.font.getData().setScale(2.5f);
		main.font.draw(main.batch, Translator.getMsg("gamename").toUpperCase(),
				Gdx.graphics.getWidth() / 2, Main.convertY(200), 0, Align.center, false);
		main.font.getData().setScale(1);

		main.font.draw(main.batch, Main.version, Gdx.graphics.getWidth() - 5, main.font.getCapHeight() * 1 + 5, 0, Align.right, false);
		if (Main.githubVersion == null) {
			main.font.draw(main.batch, Translator.getMsg("menu.checkingversion"),
					Gdx.graphics.getWidth() - 5, main.font.getCapHeight() * 2 + 5, 0, Align.right, false);
		} else {
			if (Main.githubVersion.equals(Main.version)) {
				main.font.setColor(0, 1, 0, 1);
				main.font.draw(main.batch, Translator.getMsg("menu.uptodate"),
						Gdx.graphics.getWidth() - 5, main.font.getCapHeight() * 2 + 5, 0, Align.right, false);
				main.font.setColor(1, 1, 1, 1);
			} else {
				main.font.setColor(1, 0, 0, 1);
				main.font.draw(main.batch, Translator.getMsg("menu.newversion")
						+ Main.githubVersion, Gdx.graphics.getWidth() - 5, main.font.getCapHeight() * 2 + 5, 0, Align.right, false);
				main.font.setColor(1, 1, 1, 1);
			}
		}
		container.render(main);
		main.font.setColor(Color.WHITE);
		main.batch.setColor(1, 1, 1, 1);

		main.batch.end();
	}

	@Override
	public void tickUpdate() {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

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
