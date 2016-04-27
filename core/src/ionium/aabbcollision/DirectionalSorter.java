package ionium.aabbcollision;

import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import ionium.templates.Main;

public class DirectionalSorter implements Comparator<PhysicsBody> {

	private static final Vector2 temp = new Vector2();
	private static final Vector2 temp2 = new Vector2();

	public Vector2 position = new Vector2();

	@Override
	public int compare(PhysicsBody arg0, PhysicsBody arg1) {
		arg0.bounds.getCenter(temp);
		arg1.bounds.getCenter(temp2);

		float distance1 = position.dst2(temp);
		float distance2 = position.dst2(temp2);

		if (distance1 > distance2) {
			return 1;
		} else if (distance1 < distance2) {
			return -1;
		}

		return 0;
	}

}
