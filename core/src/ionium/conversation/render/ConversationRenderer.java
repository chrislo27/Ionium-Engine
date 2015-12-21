package ionium.conversation.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

import ionium.conversation.Conversation;
import ionium.registry.AssetRegistry;
import ionium.templates.Main;
import ionium.util.AssetMap;
import ionium.util.i18n.Localization;

public class ConversationRenderer {

	private Conversation currentConv;
	private int convStage = 0;
	private int convScroll = 0;

	public ConversationRenderer() {

	}

	public void render(SpriteBatch batch, BitmapFont font, ConvStyle style, ConvSide side,
			int scrollSpeed) {
		if (currentConv == null) return;

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
		font.draw(batch,
				Localization.get(currentConv.lines[convStage].line).substring(0, convScroll),
				textStartX, textStartY + offsetY, textWidth, Align.topLeft, true);

		convScroll = MathUtils.clamp(convScroll + scrollSpeed, 0,
				Localization.get(currentConv.lines[convStage].line).length());
	}

	public void finishScrolling() {
		if (currentConv == null) return;

		convScroll = Localization.get(currentConv.lines[convStage].line).length();
	}

	public void advanceStage() {
		if (currentConv == null) return;

		convScroll = 0;
		convStage += 1;

		if (convStage >= currentConv.lines.length) {
			setToConv(null);
		}
	}

	public ConversationRenderer setToConv(Conversation conv) {
		currentConv = conv;
		convStage = 0;

		return this;
	}

}