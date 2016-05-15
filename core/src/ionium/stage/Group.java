package ionium.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;

public class Group extends Actor {

	private Array<Actor> children = new Array<>();
	private Array<Actor> pressedActors = new Array<>();

	public Group(Stage s) {
		super(s);
		setScreenOffsetSize(1, 1);
	}

	@Override
	public void onClicked(float x, float y) {
		super.onClicked(x, y);

		stage.setVectorToMouse(Gdx.input.getX(), Gdx.input.getY(), Stage.tmpVec3);

		pressedActors.clear();

		for (int i = 0; i < children.size; i++) {
			Actor act = children.get(i);
			if (act.isEnabled() && stage.isMouseOver(Stage.tmpVec3.x, Stage.tmpVec3.y, act)) {
				pressedActors.add(act);
				act.onClicked((Stage.tmpVec3.x - act.getX()) / act.getWidth(),
						(Stage.tmpVec3.y - act.getY()) / act.getHeight());
			}
		}

	}

	@Override
	public void onClickRelease(float x, float y) {
		super.onClickRelease(x, y);

		stage.setVectorToMouse(Gdx.input.getX(), Gdx.input.getY(), Stage.tmpVec3);

		for (int i = pressedActors.size - 1; i >= 0; i--) {
			Actor act = pressedActors.get(i);

			act.onClickRelease((Stage.tmpVec3.x - act.getX()) / act.getWidth(),
					(Stage.tmpVec3.y - act.getY()) / act.getHeight());

			pressedActors.removeIndex(i);
		}
	}

	@Override
	public void onMouseDrag(float x, float y) {
		super.onMouseDrag(x, y);

		checkMouseStillOnActors();
	}

	private void checkMouseStillOnActors() {
		stage.setVectorToMouse(Gdx.input.getX(), Gdx.input.getY(), Stage.tmpVec3);

		for (int i = pressedActors.size - 1; i >= 0; i--) {
			Actor act = pressedActors.get(i);

			float actorLocalX = (Stage.tmpVec3.x - act.getX()) / act.getWidth();
			float actorLocalY = (Stage.tmpVec3.y - act.getY()) / act.getHeight();

			act.onMouseDrag(actorLocalX, actorLocalY);

			if (!act.isEnabled() || !stage.isMouseOver(Stage.tmpVec3.x, Stage.tmpVec3.y, act)) {

				act.onClickRelease(actorLocalX, actorLocalY);

				pressedActors.removeIndex(i);
			}
		}
	}

	public <T extends Actor> T addActor(T actor) {
		if (actor == null) return actor;

		children.add(actor);

		return actor;
	}

	public <T extends Actor> T removeActor(T actor) {
		if (actor == null) return null;

		pressedActors.removeValue(actor, true);
		if (children.removeValue(actor, true)) return actor;

		return null;
	}

	@Override
	public void updateActualPosition() {
		super.updateActualPosition();

		checkMouseStillOnActors();

		for (int i = 0; i < children.size; i++) {
			children.get(i).getViewport().set(getX(), getY(), getWidth(), getHeight());
			children.get(i).updateActualPosition();
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		for (int i = 0; i < children.size; i++) {
			children.get(i).render(batch);
		}
	}

	@Override
	public void renderDebug(SpriteBatch batch) {
		for (int i = 0; i < children.size; i++) {
			children.get(i).renderDebug(batch);
		}

		batch.setColor(0, 1, 0, 1);
		Main.drawRect(batch, getX(), getY(), getWidth(), getHeight(), 1);
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void onResize(int width, int height) {
		super.onResize(width, height);

		for (int i = 0; i < children.size; i++) {
			children.get(i).onResize(width, height);
		}

		updateActualPosition();
	}

}