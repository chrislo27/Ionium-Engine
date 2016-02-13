package ionium.util.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TexturedQuad {

	private static float[] vertices = new float[20];
	private static float WHITE_FLOAT_BITS = new Color(1, 1, 1, 1).toFloatBits();

	public static void renderQuad(Batch batch, Texture tex, float x1, float y1, float x2, float y2,
			float u1, float v1, float u2, float v2) {
		int id = 0;
		
		vertices[id++] = x1;
		vertices[id++] = y1;
		vertices[id++] = WHITE_FLOAT_BITS;
		vertices[id++] = u1;
		vertices[id++] = v1;
		
		vertices[id++] = x1;
		vertices[id++] = y2;
		vertices[id++] = WHITE_FLOAT_BITS;
		vertices[id++] = u1;
		vertices[id++] = v2;
		
		vertices[id++] = x2;
		vertices[id++] = y1;
		vertices[id++] = WHITE_FLOAT_BITS;
		vertices[id++] = u2;
		vertices[id++] = v1;
		
		vertices[id++] = x1;
		vertices[id++] = y1;
		vertices[id++] = WHITE_FLOAT_BITS;
		vertices[id++] = u1;
		vertices[id++] = v1;
		
		batch.draw(tex, vertices, 0, vertices.length);
	}
	
	public static void renderQuad(Batch batch, TextureRegion reg, float x1, float y1, float x2, float y2){
		renderQuad(batch, reg.getTexture(), x1, y1, x2, y2, reg.getU(), reg.getV(), reg.getU2(), reg.getV2());
	}

}
