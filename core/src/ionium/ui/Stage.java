package ionium.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Stage {

	private Array<Actor> actors = new Array<>();
	private OrthographicCamera camera;

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

	public void render(SpriteBatch batch) {
		for (int i = 0; i < actors.size; i++) {
			actors.get(i).render(batch);
		}
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
