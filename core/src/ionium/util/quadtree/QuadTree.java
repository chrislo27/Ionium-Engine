package ionium.util.quadtree;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alwex on 28/05/2015.
 * 
 * Original creator: https://github.com/alwex/QuadTree
 * <br>
 * Modified to use libgdx
 */
public class QuadTree<E extends QuadRectangleable> {

	// the current nodes
	Array<E> nodes;

	// current rectangle zone
	private Rectangle zone;

	// if this is reached,
	// the zone is subdivised
	public int maxItemByNode = 5;
	public int maxLevel = 10;

	int level;

	// the four sub regions,
	// may be null if not needed
	QuadTree<E>[] regions;

	public static final int REGION_SELF = -1;
	public static final int REGION_NW = 0;
	public static final int REGION_NE = 1;
	public static final int REGION_SW = 2;
	public static final int REGION_SE = 3;

	private Array<E> tempArray = new Array<>();
	private Rectangle tempRect = new Rectangle();

	public QuadTree(Rectangle definition, int level, int maxLevel, int maxItems) {
		zone = definition;
		nodes = new Array<>();
		this.level = level;
		this.maxItemByNode = maxItems;
		this.maxLevel = maxLevel;
	}

	protected Rectangle getZone() {
		return this.zone;
	}

	private int findRegion(E rect) {
		int region = REGION_SELF;
		if (nodes.size >= maxItemByNode && this.level < maxLevel) {
			if (regions == null) {
				// then create the subregions
				this.split();
			}

			if (regions[REGION_NW].getZone().contains(rect.getX(), rect.getY())) {
				region = REGION_NW;
			} else if (regions[REGION_NE].getZone().contains(rect.getX(), rect.getY())) {
				region = REGION_NE;
			} else if (regions[REGION_SW].getZone().contains(rect.getX(), rect.getY())) {
				region = REGION_SW;
			} else if (regions[REGION_SE].getZone().contains(rect.getX(), rect.getY())) {
				region = REGION_SE;
			}
		}

		return region;
	}

	private void split() {

		regions = new QuadTree[4];

		float newWidth = zone.width / 2;
		float newHeight = zone.height / 2;
		int newLevel = level + 1;

		regions[REGION_NW] = new QuadTree<>(
				new Rectangle(zone.x, zone.y + zone.height / 2, newWidth, newHeight), newLevel,
				maxLevel, maxItemByNode);

		regions[REGION_NE] = new QuadTree<>(new Rectangle(zone.x + zone.width / 2,
				zone.y + zone.height / 2, newWidth, newHeight), newLevel, maxLevel, maxItemByNode);

		regions[REGION_SW] = new QuadTree<>(new Rectangle(zone.x, zone.y, newWidth, newHeight),
				newLevel, maxLevel, maxItemByNode);

		regions[REGION_SE] = new QuadTree<>(
				new Rectangle(zone.x + zone.width / 2, zone.y, newWidth, newHeight), newLevel,
				maxLevel, maxItemByNode);
	}

	/**
	 * Inserts an element.
	 * @param element
	 */
	public void insert(E element) {
		int region = this.findRegion(element);
		if (region == REGION_SELF || this.level == maxLevel) {
			nodes.add(element);
			return;
		} else {
			regions[region].insert(element);
		}

		if (nodes.size >= maxItemByNode && this.level < maxLevel) {
			// redispatch the elements
			tempArray.clear();
			for (E node : nodes) {
				tempArray.add(node);
			}
			nodes.clear();
			for (E node : tempArray) {
				this.insert(node);
			}
		}
	}

	/**
	 * Gets all elements in the area of E using a temp array.
	 * @param element
	 * @return
	 */
	public Array<E> getElements(E element) {
		tempArray.clear();
		return getElements(tempArray, element);
	}

	/**
	 * Gets all elements using a supplied array in the area of E. The array isn't cleared before use.
	 * @param list
	 * @param element
	 * @return
	 */
	public Array<E> getElements(Array<E> list, E element) {
		int region = this.findRegion(element);

		for (E node : nodes) {
			list.add(node);
		}

		if (region != REGION_SELF) {
			regions[region].getElements(list, element);
		} else {
			getAllElements(list, true);
		}

		return list;
	}

	/**
	 * Gets all the element in the entire tree into a supplied array. If firstCall is true,
	 * it also adds the quadtree's base elements into the list.
	 * 
	 * @param list
	 * @param firstCall
	 * @return
	 */
	public Array<E> getAllElements(Array<E> list, boolean firstCall) {
		if (regions != null) {
			regions[REGION_NW].getAllElements(list, false);
			regions[REGION_NE].getAllElements(list, false);
			regions[REGION_SW].getAllElements(list, false);
			regions[REGION_SE].getAllElements(list, false);
		}

		if (!firstCall) {
			for (E node : nodes) {
				list.add(node);
			}
		}

		return list;
	}

	/**
	 * Returns all rectangle bounding areas.
	 * @param list
	 */
	public void getAllZones(Array<Rectangle> list) {
		list.add(this.zone);
		if (regions != null) {
			regions[REGION_NW].getAllZones(list);
			regions[REGION_NE].getAllZones(list);
			regions[REGION_SW].getAllZones(list);
			regions[REGION_SE].getAllZones(list);
		}
	}
}