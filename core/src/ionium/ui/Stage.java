package ionium.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;

public class Stage {

	private Array<Actor> actors = new Array<>();
	private OrthographicCamera camera;

	public boolean debugMode = false;

	public Stage() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public <T extends Actor> T addActor(T actor) {
		if (actor == null) return actor;

		actors.add(actor);

		return actor;
	}

	public <T extends Actor> T removeActor(T actor) {
		if (actor == null) return null;

		if (actors.removeValue(actor, true)) return actor;

		return null;
	}

	/**
	 * Sets the batch projection matrix to the stage's camera matrix and renders
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);

		for (int i = 0; i < actors.size; i++) {
			actors.get(i).render(batch);
		}

		if (!debugMode) return;

		batch.setColor(1, 0, 0, 1);
		Main.drawRect(batch, 0, 0, camera.viewportWidth, camera.viewportHeight, 1);

		for (int i = 0; i < actors.size; i++) {
			Actor act = actors.get(i);

			act.renderDebug(batch);
		}

		batch.setColor(1, 1, 1, 1);
	}

	public void onResize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();

		for (int i = 0; i < actors.size; i++) {
			actors.get(i).onResize(width, height);
		}
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

}
