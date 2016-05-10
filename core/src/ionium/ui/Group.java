package ionium.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Group extends Actor {

	private Array<Actor> children = new Array<>();

	public Group(Stage s) {
		super(s);
		setScreenOffsetSize(1, 1);
	}

	public <T extends Actor> T addActor(T actor) {
		if (actor == null) return actor;

		children.add(actor);

		return actor;
	}

	public <T extends Actor> T removeActor(T actor) {
		if (actor == null) return null;

		if (children.removeValue(actor, true)) return actor;

		return null;
	}

	@Override
	public void updateActualPosition() {
		super.updateActualPosition();

		for (int i = 0; i < children.size; i++) {
			children.get(i).getViewport().set(actualX(), actualY(), actualWidth(), actualHeight());
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
	public void onResize(int width, int height) {
		super.onResize(width, height);

		for (int i = 0; i < children.size; i++) {
			children.get(i).onResize(width, height);
		}

		updateActualPosition();
	}

}
