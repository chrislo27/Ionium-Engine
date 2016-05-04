package ionium.aabbcollision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ionium.templates.Main;
import ionium.util.MathHelper;

public class CollisionResolver {

	private final Pool<CollisionResult> resultPool = new Pool<CollisionResult>() {

		@Override
		protected CollisionResult newObject() {
			return new CollisionResult();
		}

	};
	private final Array<PhysicsBody> tempBodies = new Array<>();
	private Vector2 tempAmtToMove = new Vector2();
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
	public CollisionResult resolveCollisionBetweenBodies(PhysicsBody target,
			Array<PhysicsBody> otherBodies, Rectangle pathBounds) {

		// obtain pooled result
		CollisionResult result = resultPool.obtain();

		// set up the sorter to sort from the position of the target's centre
		target.bounds.getCenter(sorter.position);
		otherBodies.sort(sorter);

		float remainingVeloX = target.velocity.x * timeScale;
		float remainingVeloY = target.velocity.y * timeScale;
		float positionX = target.bounds.x;
		float positionY = target.bounds.y;
		float halfWidth = target.bounds.width * 0.5f;
		float halfHeight = target.bounds.height * 0.5f;

		// a step is moving at max half size or remainder of the velocity
		// the steps must still follow slope though

		outerLoop: while (Math.abs(remainingVeloX) > 0 || Math.abs(remainingVeloY) > 0) {
			result.stepsTaken++;

			// scan bodies for collision
			for (PhysicsBody b : otherBodies) {
				if (MathHelper.intersects(positionX, positionY, target.bounds.width,
						target.bounds.height, b.bounds.x, b.bounds.y, b.bounds.width,
						b.bounds.height, false)) {
					// collision detected
					// let's resolve it one axis at a time
					// per axis: kill velocity (target, remaining), set position and normals
					// only if we're inside the body do we have to correct the position

					// find smallest axis of intersection
					float xOverlap = 0;
					float yOverlap = 0;
					Vector2 normal = result.normal;

					if (target.velocity.x != 0) {
						xOverlap = (positionX + (target.velocity.x > 0 ? target.bounds.width : 0))
								- (b.bounds.x + (target.velocity.x < 0 ? b.bounds.width : 0));
					}

					if (target.velocity.y != 0) {
						yOverlap = (positionY + (target.velocity.y > 0 ? target.bounds.height : 0))
								- (b.bounds.y + (target.velocity.y < 0 ? b.bounds.height : 0));
					}

					if (Math.abs(xOverlap) >= b.bounds.width
							&& Math.abs(yOverlap) >= b.bounds.height)
						continue;

					// normals
					normal.setZero();

					if (Math.abs(xOverlap) <= Math.abs(yOverlap)
							|| (yOverlap == 0 && xOverlap != 0)) {
						normal.x = -Math.signum(xOverlap);
					}

					if (Math.abs(yOverlap) <= Math.abs(xOverlap)
							|| (xOverlap == 0 && yOverlap != 0)) {
						normal.y = -Math.signum(yOverlap);
					}

					// actual resolution
					if (normal.x == 1) {
						positionX = b.bounds.x + b.bounds.width;
						remainingVeloX = 0;
						target.velocity.x = 0;
					} else if (normal.x == -1) {
						positionX = b.bounds.x - target.bounds.width;
						remainingVeloX = 0;
						target.velocity.x = 0;
					}

					if (normal.y == 1) {
						positionY = b.bounds.y + b.bounds.height;
						remainingVeloY = 0;
						target.velocity.y = 0;
					} else if (normal.y == -1) {
						positionY = b.bounds.y - target.bounds.height;
						remainingVeloY = 0;
						target.velocity.y = 0;
					}

					break outerLoop;
				} else {
					// check for direct touches

					if (positionY + target.bounds.height > b.bounds.y
							&& positionY < b.bounds.y + b.bounds.height) {
						if ((positionX + target.bounds.width == b.bounds.x && remainingVeloX > 0)
								|| (positionX == b.bounds.x + b.bounds.width
										&& remainingVeloX < 0)) {
							remainingVeloX = 0;
							target.velocity.x = 0;
						}
					}

					if (positionX + target.bounds.width > b.bounds.x
							&& positionX < b.bounds.x + b.bounds.width) {
						if ((positionY + target.bounds.height == b.bounds.y && remainingVeloY > 0)
								|| (positionY == b.bounds.y + b.bounds.height
										&& remainingVeloY < 0)) {
							remainingVeloY = 0;
							target.velocity.y = 0;
						}
					}
				}
			}

			// move position and remaining velocity
			// temp
			positionX += remainingVeloX;
			positionY += remainingVeloY;

			remainingVeloX = 0;
			remainingVeloY = 0;
		}

		// set position on result
		result.newPosition.set(positionX, positionY);

		return result;
	}

	public Array<PhysicsBody> getTempBodyArray() {
		tempBodies.clear();

		return tempBodies;
	}

	public void freeResult(CollisionResult r) {
		resultPool.free(r);
	}

}
