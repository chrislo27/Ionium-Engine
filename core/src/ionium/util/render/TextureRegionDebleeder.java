package ionium.util.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionDebleeder {

	public static float fixAmount = 0.01f;

	public static void fixBleeding(TextureRegion region) {
		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + fixAmount) * invTexWidth, (y + fixAmount) * invTexHeight,
				(x + width - fixAmount) * invTexWidth, (y + height - fixAmount) * invTexHeight);
	}

}
