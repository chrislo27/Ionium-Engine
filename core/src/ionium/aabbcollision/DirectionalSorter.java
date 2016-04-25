package ionium.aabbcollision;

import java.util.Comparator;

public class DirectionalSorter implements Comparator<PhysicsBody> {

	public float velox = 0;
	public float veloy = 0;

	@Override
	public int compare(PhysicsBody arg0, PhysicsBody arg1) {
		if (arg1.bounds.x - arg0.bounds.x != 0) {
			if (velox > 0) {
				if (arg1.bounds.x - arg0.bounds.x > 0) {
					return -1;
				} else {
					return 1;
				}
			}

			if (velox < 0) {
				if (arg1.bounds.x - arg0.bounds.x < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		}

		if (arg1.bounds.y - arg0.bounds.y != 0) {
			if (veloy > 0) {
				if (arg1.bounds.y - arg0.bounds.y > 0) {
					return -1;
				} else {
					return 1;
				}
			}

			if (veloy < 0) {
				if (arg1.bounds.y - arg0.bounds.y < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		}

		return 0;
	}

}
