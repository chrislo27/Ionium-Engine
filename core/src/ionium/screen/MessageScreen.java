package ionium.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import ionium.registry.GlobalVariables;
import ionium.templates.Main;

public class MessageScreen extends Updateable {

	private BitmapFont font;
	
	public MessageScreen(Main m, BitmapFont font) {
		super(m);
		this.font = font;
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

		float width = (GlobalVariables.getInt("DEFAULT_WIDTH") / 3f) * 2f;
		main.defaultFont.draw(main.batch, getRenderMessage(),
				(GlobalVariables.getInt("DEFAULT_WIDTH") - width) / 2f,
				Main.convertY((GlobalVariables.getInt("DEFAULT_HEIGHT") / 3f)), width, Align.center, true);

		container.render(main, font);
		main.defaultFont.setColor(Color.WHITE);
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
	public Array<String> getDebugStrings(Array<String> array) {
		return array;
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
