package ionium.aabbcollision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;
import ionium.util.MathHelper;

public class CollisionResolver {

	private final Vector2 tempVector = new Vector2();
	private final Array<PhysicsBody> tempBodies = new Array<>();
	private final DirectionalSorter sorter = new DirectionalSorter();

	public float timeScale = 1;
	private boolean wasLastResolutionACollision = false;

	public CollisionResolver(float timeScale) {
		this.timeScale = timeScale;
	}

	/**
	 * Returns resolution point of collision, or the eventual target position
	 * @param target
	 * @return
	 */
	public Vector2 resolveCollisionBetweenBodies(PhysicsBody target,
			Array<PhysicsBody> otherBodies) {
		// set final position if no collision occurs
		tempVector.set(target.bounds.x + target.velocity.x * timeScale,
				target.bounds.y + target.velocity.y * timeScale);
		wasLastResolutionACollision = false;

		sorter.velox = target.velocity.x;
		sorter.veloy = target.velocity.y;
		otherBodies.sort(sorter);

		for (PhysicsBody b : otherBodies) {
			if (b.mayBeHitInPath(timeScale, target)) {
				float hitX;
				float hitY;

				if (Math.abs(target.velocity.x) >= Math.abs(target.velocity.y)) {
					hitX = calcHitX(target, b, 0, true);
					hitY = calcHitY(target, b, hitX, false);
				} else {
					hitY = calcHitY(target, b, 0, true);
					hitX = calcHitX(target, b, hitY, false);
				}

				if (hitX == b.bounds.x + b.bounds.width || hitX + target.bounds.width == b.bounds.x
						|| hitY == b.bounds.y + b.bounds.height
						|| hitY + target.bounds.height == b.bounds.y) {
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
