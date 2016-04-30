package ionium.aabbcollision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;

public class CollisionResolver {

	private final Vector2 tempVector = new Vector2();
	private final Array<PhysicsBody> tempBodies = new Array<>();
	private final DirectionalSorter sorter = new DirectionalSorter();

	public float timeScale = 1;
	public float tolerance = 1f / 100f;
	private boolean wasLastResolutionACollision = false;

	public CollisionResolver(float timeScale, float tolerance) {
		this.timeScale = timeScale;
		this.tolerance = tolerance;
	}

	/**
	 * Returns resolution point of collision, or the eventual target position
	 * @param target
	 * @return
	 */
	public Vector2 resolveCollisionBetweenBodies(PhysicsBody target, Array<PhysicsBody> otherBodies,
			Rectangle pathBounds) {
		// set final position if no collision occurs
		tempVector.set(target.bounds.x + target.velocity.x * timeScale,
				target.bounds.y + target.velocity.y * timeScale);
		wasLastResolutionACollision = false;

		target.bounds.getCenter(sorter.position);
		otherBodies.sort(sorter);

		for (PhysicsBody b : otherBodies) {
			if (b.bounds.overlaps(pathBounds)) {
				float hitX;
				float hitY;

				if (Math.abs(target.velocity.x) >= Math.abs(target.velocity.y)) {
					hitX = calcHitX(target, b, 0, true);
					hitY = calcHitY(target, b, hitX, false);
				} else {
					hitY = calcHitY(target, b, 0, true);
					hitX = calcHitX(target, b, hitY, false);
				}

				if ((MathUtils.isEqual(hitX, b.bounds.x + b.bounds.width, tolerance)
						|| MathUtils.isEqual(hitX + target.bounds.width, b.bounds.x, tolerance)
						|| MathUtils.isEqual(hitY, b.bounds.y + b.bounds.height)
						|| MathUtils.isEqual(hitY + target.bounds.height, b.bounds.y, tolerance))) {

					tempVector.set(hitX, hitY);
					wasLastResolutionACollision = true;

					break;
				}
			}
		}

		return tempVector;
	}

	public boolean wasLastResolutionACollision() {
		return wasLastResolutionACollision;
	}

	private float calcHitX(PhysicsBody target, PhysicsBody other, final float hitY,
			boolean isFirst) {
		float hitX;

		if (isFirst) {
			if (target.velocity.x >= 0) {
				hitX = other.bounds.x - target.bounds.width;
			} else {
				hitX = other.bounds.x + other.bounds.width;
			}
		} else {
			hitX = target.bounds.x + ((target.velocity.x * timeScale)
					* ((hitY - target.bounds.y) / (target.velocity.y * timeScale)));
		}

		return hitX;
	}

	private float calcHitY(PhysicsBody target, PhysicsBody other, final float hitX,
			boolean isFirst) {
		float hitY;

		if (isFirst) {
			if (target.velocity.y >= 0) {
				hitY = other.bounds.y - target.bounds.height;
			} else {
				hitY = other.bounds.y + other.bounds.height;
			}
		} else {
			hitY = target.bounds.y + ((target.velocity.y * timeScale)
					* ((hitX - target.bounds.x) / (target.velocity.x * timeScale)));
		}

		return hitY;
	}

	public Array<PhysicsBody> getTempBodyArray() {
		tempBodies.clear();

		return tempBodies;
	}

}
