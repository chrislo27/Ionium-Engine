package ionium.util.quadtree;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by samsung on 28/05/2015.
 */
public class QuadNode<T> {

	Rectangle r;
	T element;

	QuadNode(Rectangle r, T element) {
		this.r = r;
		this.element = element;
	}

	@Override
	public String toString() {
		return r.toString();
	}
}