package ionium.conversation.render;


public class ConvStyle {
	
	public boolean shouldFaceBeShown = true;
	public boolean shouldFaceBeRightAligned = false;
	public boolean shouldPlayMumbling = true;
	public float mumblingPitchOffset = 0.1f;
	public float textPadding = 32;
	public float percentageOfScreenToOccupy = 0.15f;
	
	/**
	 * Retain default values
	 */
	public ConvStyle(){
		
	}

	public ConvStyle(boolean shouldFaceBeShown, boolean shouldFaceBeRightAligned,
			boolean shouldPlayMumbling, float mumblingPitchOffset, float textPadding,
			float percentageOfScreenToOccupy) {
		this.shouldFaceBeShown = shouldFaceBeShown;
		this.shouldFaceBeRightAligned = shouldFaceBeRightAligned;
		this.shouldPlayMumbling = shouldPlayMumbling;
		this.mumblingPitchOffset = mumblingPitchOffset;
		this.textPadding = textPadding;
		this.percentageOfScreenToOccupy = percentageOfScreenToOccupy;
	}
	
}
