package ionium.conversation.render;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class ConvStyle {
	
	public boolean shouldFaceBeShown = true;
	public boolean shouldFaceBeRightAligned = false;
	public boolean shouldPlayMumbling = true;
	public float mumblingPitchOffset = 0.1f;
	public float textPaddingX = 32f / 1920f;
	public float textPaddingY = 18f / 1080f;
	public float percentageOfScreenToOccupy = 0.25f;
	
	/**
	 * Retain default values
	 */
	public ConvStyle(){
		
	}

	public ConvStyle(boolean shouldFaceBeShown, boolean shouldFaceBeRightAligned,
			boolean shouldPlayMumbling, float mumblingPitchOffset, float textPaddingX,
			float textPaddingY, float percentageOfScreenToOccupy) {
		this.shouldFaceBeShown = shouldFaceBeShown;
		this.shouldFaceBeRightAligned = shouldFaceBeRightAligned;
		this.shouldPlayMumbling = shouldPlayMumbling;
		this.mumblingPitchOffset = mumblingPitchOffset;
		this.textPaddingX = textPaddingX;
		this.textPaddingY = textPaddingY;
		this.percentageOfScreenToOccupy = percentageOfScreenToOccupy;
	}
	
}
