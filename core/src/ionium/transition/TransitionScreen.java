package ionium.transition;

import ionium.screen.Updateable;
import ionium.templates.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

public class TransitionScreen extends Updateable<Main> {

	public TransitionScreen(Main m) {
		super(m);
	}

	Transition from = null;
	Transition to = null;

	Screen previousScreen = null;
	Screen nextScreen = null;

	boolean onTo = false;

	@Override
	public void render(float delta) {

		if (to == null) {
			if (onTo) main.setScreen(nextScreen);
		} else {
			if ((to.finished() && onTo)) main.setScreen(nextScreen);
		}

		if (from == null) {
			onTo = true;
		} else {
			if (from.finished()) {
				onTo = true;
			}
		}

		if (onTo) {
			if (nextScreen != null) nextScreen.render(delta);
			main.batch.begin();
			if (to != null) to.render(main);
			main.batch.end();
		} else {
			if (previousScreen != null && previousScreen != this) previousScreen.render(delta);
			main.batch.begin();
			from.render(main);
			main.batch.end();
		}

	}

	@Override
	public void tickUpdate() {
		if (onTo) {
			if (to != null) to.tickUpdate(main);
		} else {
			if (from != null) from.tickUpdate(main);
		}
	}

	public void prepare(Screen p, Transition f, Transition t, Screen n) {
		from = f;
		previousScreen = p;
		to = t;
		nextScreen = n;
		onTo = false;
		if (from == null) {
			onTo = true;
		}
	}

	@Override
	public Array<String> getDebugStrings(Array<String> array) {
		array.add("prevScreen: " + (previousScreen == null ? null : previousScreen.getClass().getSimpleName()));
		array.add("nextScreen: " + (nextScreen == null ? null : nextScreen.getClass().getSimpleName()));
		array.add("[" + (!onTo ? "GREEN" : "RED") + "]fromTransition: " + (from == null ? null : from.getClass().getSimpleName()) + "[]");
		array.add("[" + (onTo ? "GREEN" : "RED") + "]toTransition: " + (to == null ? null : to.getClass().getSimpleName()) + "[]");
		
		return array;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(main.getDefaultInput());
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
	public void renderUpdate() {
	}

}
