package ionium.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple quadtree implementation that allows its objects to have rectangular bounds (not just points) and also allows
 * to query all objects within a rectangular area.
 * 
 * @author Kai Burjack, KaiHH on java-gaming.org
 *
 * @param <T>
 *            the type of objects in this {@link QuadTree}. Must implement {@link Boundable}
 */
public class QuadTree<T extends QuadTree.Boundable> {

	/**
	 * A simple rectangle. Will describe the bounds of an object in the quadtree.
	 */
	public static class Rectangle {

		public float x;
		public float y;
		public float width;
		public float height;

		public Rectangle() {
		}

		public Rectangle(float x, float y, float width, float height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	/**
	 * Abstract interface of something that we can put into the quadtree.
	 */
	public interface Boundable {

		Rectangle getBounds();
	}

	// Constants for the quadrants of the quadtree
	private static final int PXNY = 0;
	private static final int NXNY = 1;
	private static final int NXPY = 2;
	private static final int PXPY = 3;

	private Rectangle bounds;
	private ArrayList<T> objects;
	private QuadTree<T>[] children;
	
	private int maxObjectsPerNode = 64;

	public QuadTree(int maxObjs, Rectangle b) {
		bounds = b;
		maxObjectsPerNode = 64;
	}

	private void split() {
		float hw = bounds.width / 2.0f;
		float hh = bounds.height / 2.0f;
		float x = bounds.x;
		float y = bounds.y;
		children = new QuadTree[4];
		children[NXNY] = new QuadTree<T>(maxObjectsPerNode, new Rectangle(x, y, hw, hh));
		children[PXNY] = new QuadTree<T>(maxObjectsPerNode, new Rectangle(x + hw, y, hw, hh));
		children[NXPY] = new QuadTree<T>(maxObjectsPerNode, new Rectangle(x, y + hh, hw, hh));
		children[PXPY] = new QuadTree<T>(maxObjectsPerNode, new Rectangle(x + hw, y + hh, hw, hh));
	}

	private boolean insertIntoChild(T o) {
		// we can insert the object into a child if the object's bounds are
		// completely within any of the child bounds.
		Rectangle r = o.getBounds();
		float xm = bounds.x + bounds.width / 2.0f;
		float ym = bounds.y + bounds.height / 2.0f;
		boolean inserted = false;
		if (r.x >= xm && r.x + r.width < bounds.x + bounds.width) {
			if (r.y >= ym && r.y + r.height < bounds.y + bounds.height) {
				inserted = children[PXPY].insert(o);
			} else if (r.y >= bounds.y && r.y + r.height < ym) {
				inserted = children[PXNY].insert(o);
			}
		} else if (r.x >= bounds.x && r.x + r.width < xm) {
			if (r.y >= ym && r.y + r.height < bounds.y + bounds.height) {
				inserted = children[NXPY].insert(o);
			} else if (r.y >= bounds.y && r.y + r.height < ym) {
				inserted = children[NXNY].insert(o);
			}
		}
		return inserted;
	}

	/**
	 * @return <code>true</code> if the given object could be inserted anywhere; or <code>false</code> if not
	 */
	public boolean insert(T object) {
		if (children != null && insertIntoChild(object)) return true;
		if (objects != null && objects.size() == maxObjectsPerNode) {
			// too many objects in this quadtree level
			if (children == null) {
				// split this quadtree once
				split();
				// and try to redistribute the objects into the children
				for (int i = 0; i < objects.size(); i++) {
					if (insertIntoChild(objects.get(i))) {
						// succeeded with that one -> it fitted within a child!
						objects.remove(i);
						i--;
					}
				}
			}
			if (!insertIntoChild(object)) {
				// cannot distribute the object to any child
				if (objects.size() == maxObjectsPerNode) {
					// and we are still full!
					// -> cannot insert
					return false;
				} else {
					objects.add(object);
				}
			}
		} else {
			if (objects == null) objects = new ArrayList<T>();
			objects.add(object);
		}
		return true;
	}

	public int query(Rectangle r, List<T> res) {
		return query(r, res, false);
	}

	public int query(Rectangle r, List<T> res, boolean countOnly) {
		int count = 0;
		if (children != null) {
			// query children
			float xm = bounds.x + bounds.width / 2.0f;
			float ym = bounds.y + bounds.height / 2.0f;
			boolean intersectsNx = r.x < xm && r.x + r.width >= bounds.x;
			boolean intersectsPx = r.x < bounds.x + bounds.width && r.x + r.width >= xm;
			boolean intersectsNy = r.y < ym && r.y + r.height >= bounds.y;
			boolean intersectsPy = r.y < bounds.y + bounds.height && r.y + r.height >= ym;
			if (intersectsNy) {
				if (intersectsPx) count += children[PXNY].query(r, res, countOnly);
				if (intersectsNx) count += children[NXNY].query(r, res, countOnly);
			}
			if (intersectsPy) {
				if (intersectsPx) count += children[PXPY].query(r, res, countOnly);
				if (intersectsNx) count += children[NXPY].query(r, res, countOnly);
			}
		}
		if (objects != null) {
			// query objects in this level
			if (r.x < bounds.x && r.y < bounds.y && r.x + r.width >= bounds.x + bounds.width
					&& r.y + r.height >= bounds.y + bounds.height) {
				// node lies completely within query -> simply add all objects
				count += objects.size();
				if (!countOnly) res.addAll(objects);
			} else {
				// must check each object individually
				for (int i = 0; i < objects.size(); i++) {
					T o = objects.get(i);
					Rectangle bounds = o.getBounds();
					if (r.x < bounds.x + bounds.width && r.x + r.width >= bounds.x
							&& r.y < bounds.y + bounds.height && r.y + r.height >= bounds.y) {
						count++;
						if (!countOnly) res.add(o);
					}
				}
			}
		}
		return count;
	}

}