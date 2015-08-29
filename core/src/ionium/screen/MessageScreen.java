package ionium.screen;

import ionium.templates.Main;
import ionium.templates.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;

public class MessageScreen extends Updateable {

	public MessageScreen(Main m) {
		super(m);
	}

	public void setMessage(String s) {
		message = s;
	}

	private String message = "uninitialized message";

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();

		float width = (Settings.DEFAULT_WIDTH / 3f) * 2f;
		main.font.draw(main.batch, getRenderMessage(),
				(Settings.DEFAULT_WIDTH - width) / 2f,
				Main.convertY((Settings.DEFAULT_HEIGHT / 3f)), width, Align.center, true);

		container.render(main);
		main.font.setColor(Color.WHITE);
		main.batch.setColor(1, 1, 1, 1);

		main.batch.end();
	}

	public String getMessage() {
		return message;
	}

	public String getRenderMessage() {
		return message;
	}

	@Override
	public void renderUpdate() {
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
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

}
