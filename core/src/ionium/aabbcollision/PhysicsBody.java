package ionium.aabbcollision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ionium.templates.Main;
import ionium.util.MathHelper;

public class PhysicsBody {

	private static final Rectangle pathHitTempRect = new Rectangle();

	public Rectangle bounds = new Rectangle();
	public Vector2 velocity = new Vector2();

	public PhysicsBody() {
		this(0, 0, 1, 1);
	}

	public PhysicsBody(float x, float y, float w, float h) {
		setBounds(x, y, w, h);
	}

	public PhysicsBody setBounds(float x, float y, float w, float h) {
		bounds.set(x, y, w, h);

		return this;
	}

	public PhysicsBody setVelocity(float x, float y) {
		velocity.set(x, y);

		return this;
	}

	public boolean mayBeHitInPath(float timeScale, PhysicsBody other) {
		// not moving = not going to get hit
		if (other.velocity.isZero()) return false;

		pathHitTempRect.set(other.bounds.x, other.bounds.y,
				other.velocity.x * timeScale + other.bounds.width,
				other.velocity.y * timeScale + other.bounds.height);
		MathHelper.normalizeRectangle(pathHitTempRect);

		return bounds.overlaps(pathHitTempRect) || pathHitTempRect.overlaps(bounds);
	}

	@Override
	public String toString() {
		return "[" + bounds.toString() + ", " + velocity.toString() + "]";
	}

}
