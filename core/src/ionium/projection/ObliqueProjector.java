package ionium.projection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import ionium.projection.CoordinateF.CoordFPool;
import ionium.util.render.TexturedQuad;

public class ObliqueProjector {

	public float xDepth = 0.5f;
	public float yDepth = -0.2f;

	private final Batch batch;

	public ObliqueProjector(Batch b) {
		batch = b;
	}

	// render order
	/*
	 * left to right
	 * bottom to top
	 * back to front
	 */

	/**
	 * Parameters passed in act like an orthographic view.
	 * @param tex
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 */
	public void render(RenderFace face, Texture tex, float x, float y, float z, float width,
			float height) {
		CoordinateF coord1 = convertToProjected(x, y, z);
		CoordinateF coord2 = convertToProjected(x + width, y, z);
		CoordinateF coord3 = convertToProjected(x + width, y + height, z);
		CoordinateF coord4 = convertToProjected(x, y + height, z);

		if (face == RenderFace.SIDE) {
			coord1.set(coord2.x, coord2.y);
			coord4.set(coord3.x, coord3.y);
			coord2.translateToDepth(width, height, xDepth);
			coord3.translateToDepth(width, height, xDepth);
		} else if (face == RenderFace.TOP) {
			coord1.setY(coord4.y);
			coord2.setY(coord3.y);
			coord3.set(coord2.x, coord2.y).translateToDepth(width, height, xDepth);
			coord4.set(coord1.x, coord1.y).translateToDepth(width, height, xDepth);
		}

		TexturedQuad.renderQuad(batch, tex, coord1.x, coord1.y, coord2.x, coord2.y, coord3.x,
				coord3.y, coord4.x, coord4.y, 0, 0, 1, 1);

		CoordFPool.pool.free(coord1);
		CoordFPool.pool.free(coord2);
		CoordFPool.pool.free(coord3);
		CoordFPool.pool.free(coord4);
	}

	/**
	 * Converts from world to screen
	 * @param coord
	 * @return
	 */
	public CoordinateF convertToProjected(CoordinateF coord, float x, float y, float z) {
		coord.setX(x);
		coord.setY(y + (x * yDepth));

		coord.set(coord.x + (z * xDepth), coord.y + (z * xDepth));

		return coord;
	}

	public CoordinateF convertToProjected(float x, float y, float z) {
		return convertToProjected(CoordFPool.pool.obtain(), x, y, z);
	}

	public static enum RenderFace {
		FRONT, SIDE, TOP;
	}

}
