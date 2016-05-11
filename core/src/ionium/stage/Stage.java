package ionium.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;
import ionium.util.MathHelper;

public class Stage implements InputProcessor {

	private static final Vector3 tmpVec3 = new Vector3();

	private Array<Actor> actors = new Array<>();
	private Array<Actor> pressedActors = new Array<>();
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

		pressedActors.removeValue(actor, true);
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

	// input processor stuff

	public void addSelfToInputMultiplexer(InputMultiplexer plex) {
		plex.addProcessor(0, this);
	}

	public void removeSelfFromInputMultiplexer(InputMultiplexer plex) {
		plex.removeProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		tmpVec3.set(camera.unproject(tmpVec3.set(screenX, screenY, 0)));

		if (button == Buttons.LEFT) {
			pressedActors.clear();

			for (int i = 0; i < actors.size; i++) {
				Actor act = actors.get(i);
				if (MathHelper.intersects(tmpVec3.x, tmpVec3.y, 1, 1, act.actualX(), act.actualY(),
						act.actualWidth(), act.actualHeight(), true)) {
					pressedActors.add(act);
					act.onClicked((tmpVec3.x - act.actualX()) / act.actualWidth(),
							(tmpVec3.y - act.actualY()) / act.actualHeight());
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		tmpVec3.set(camera.unproject(tmpVec3.set(screenX, screenY, 0)));

		if (button == Buttons.LEFT) {
			for (int i = pressedActors.size - 1; i >= 0; i--) {
				Actor act = pressedActors.get(i);

				act.onClickRelease((tmpVec3.x - act.actualX()) / act.actualWidth(),
						(tmpVec3.y - act.actualY()) / act.actualHeight());

				pressedActors.removeIndex(i);
			}
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		tmpVec3.set(camera.unproject(tmpVec3.set(screenX, screenY, 0)));

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			for (int i = pressedActors.size - 1; i >= 0; i--) {
				Actor act = pressedActors.get(i);

				if (!MathHelper.intersects(tmpVec3.x, tmpVec3.y, 1, 1, act.actualX(), act.actualY(),
						act.actualWidth(), act.actualHeight(), true)) {

					act.onClickRelease((tmpVec3.x - act.actualX()) / act.actualWidth(),
							(tmpVec3.y - act.actualY()) / act.actualHeight());

					pressedActors.removeIndex(i);
				}
			}
		}

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
