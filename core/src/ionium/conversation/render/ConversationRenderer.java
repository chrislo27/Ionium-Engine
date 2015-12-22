package ionium.conversation.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

import ionium.conversation.Conversation;
import ionium.conversation.Voice;
import ionium.registry.AssetRegistry;
import ionium.templates.Main;
import ionium.util.AssetMap;
import ionium.util.i18n.Localization;

public class ConversationRenderer {

	private Conversation currentConv;
	private int convStage = 0;
	private int convScroll = 0;

	private int selectionIndex = 0;

	private float renderTime = -1;

	public ConversationRenderer() {

	}

	public void render(SpriteBatch batch, BitmapFont font, ConvStyle style, ConvSide side,
			int scrollSpeed) {
		if (currentConv == null) return;
		
		if(renderTime <= -1){
			renderTime = currentConv.lines[convStage].character.voice.avgLength;
		}

		float textStartX = style.textPadding;
		float textStartY = (Gdx.graphics.getHeight() * style.percentageOfScreenToOccupy)
				- style.textPadding;
		float offsetY = 0;
		float textWidth = Gdx.graphics.getWidth() - (style.textPadding * 2);

		if (side == ConvSide.TOP) {
			offsetY = (1.0f - style.percentageOfScreenToOccupy) * Gdx.graphics.getHeight();
		}

		if (style.shouldFaceBeShown && currentConv.lines[convStage].character.face != null) {
			float faceWidth = AssetRegistry
					.getTexture(AssetMap.get(currentConv.lines[convStage].character.face))
					.getWidth();

			if (style.shouldFaceBeRightAligned) {
				textWidth -= faceWidth;
				textWidth -= style.textPadding;
			} else {
				textStartX += faceWidth;
				textStartX += style.textPadding;

				textWidth -= faceWidth - style.textPadding;
			}
		}

		batch.setColor(0, 0, 0, 0.5f);
		Main.fillRect(batch, 0, offsetY, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight() * style.percentageOfScreenToOccupy);
		batch.setColor(1, 1, 1, 1);

		if (style.shouldFaceBeShown && currentConv.lines[convStage].character.face != null) {

			float faceX = style.textPadding;
			Texture tex = AssetRegistry
					.getTexture(AssetMap.get(currentConv.lines[convStage].character.face));

			if (style.shouldFaceBeRightAligned) {
				faceX += textWidth;
			}

			batch.draw(tex, faceX, textStartY + offsetY - tex.getHeight());
		}

		font.setColor(1, 1, 1, 1);
		font.draw(batch, getActualMessage().substring(0, convScroll), textStartX,
				textStartY + offsetY, textWidth, Align.topLeft, true);

		// voice
		if (style.shouldPlayMumbling && convScroll < getActualMessage().length()) {
			Voice voice = currentConv.lines[convStage].character.voice;
			Sound sound = AssetRegistry.getSound(voice.voiceFile);

			if (voice != null) {
				if (renderTime >= voice.avgLength) {
					renderTime -= voice.avgLength;

					float pitchOffset = MathUtils.random(0, style.mumblingPitchOffset)
							* MathUtils.randomSign();
					if (pitchOffset < 0) {
						// because it ranges from 0.5 to 2.0, the high end is x2, the low end is x0.5
						pitchOffset *= 0.5f;
					}

					sound.play(1f, 1f + pitchOffset, 0);
				}
			}
		}

		// scrolling
		convScroll = MathUtils.clamp(convScroll + scrollSpeed, 0, getActualMessage().length());
		renderTime += Gdx.graphics.getDeltaTime();
	}

	public void inputUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (isFinishedScrolling()) {
				advanceStage();
			} else {
				finishScrolling();
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.A) || Gdx.input.isKeyJustPressed(Keys.LEFT)) {

		} else if (Gdx.input.isKeyJustPressed(Keys.D) || Gdx.input.isKeyJustPressed(Keys.RIGHT)) {

		}
	}

	public boolean isInConv() {
		return currentConv != null;
	}

	public String getActualMessage() {
		if (currentConv == null) return "";

		return Localization.get(currentConv.lines[convStage].line);
	}

	public boolean isFinishedScrolling() {
		if (currentConv == null) return true;

		return convScroll == getActualMessage().length();
	}

	public void finishScrolling() {
		if (currentConv == null) return;

		convScroll = getActualMessage().length();
	}

	public void advanceStage() {
		if (currentConv == null) return;

		convScroll = 0;
		selectionIndex = 0;
		convStage += 1;
		renderTime = -1;

		if (convStage >= currentConv.lines.length) {
			setToConv(null);
		}
	}

	public ConversationRenderer setToConv(Conversation conv) {
		currentConv = conv;
		convStage = 0;
		selectionIndex = -1;

		return this;
	}

}
