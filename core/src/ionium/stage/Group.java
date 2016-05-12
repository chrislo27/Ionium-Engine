package ionium.stage;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;

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
