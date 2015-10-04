package ionium.menutree;

import ionium.templates.Main;
import ionium.util.Utils;
import ionium.util.controllers.Xbox360Controllers;
import ionium.util.controllers.Xbox360Mappings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * The container class for the MenuElements in a screen.
 * 
 *
 */
public class MenuTree {

	private Array<MenuElement> recomputationQueue = new Array<>();
	private Array<MenuElement> elements = new Array<>();
	private int totalElementsCount = 0;

	private int selected = 0;

	private float x, y;
	private float indent = 32;

	private Main main;

	private ControllerAdapter controllerAdapter = new ControllerAdapter() {

		@Override
		public boolean buttonDown(Controller controller, int buttonIndex) {
			if (controller == Xbox360Controllers.getController(0)) {
				if (buttonIndex == Xbox360Mappings.BUTTON_A) {
					pressEnter();
				} else if (buttonIndex == Xbox360Mappings.BUTTON_B
						|| buttonIndex == Xbox360Mappings.BUTTON_BACK) {
					pressBack();
				}
			}
			return true;
		}

		@Override
		public boolean buttonUp(Controller controller, int buttonIndex) {
			return false;
		}

		@Override
		public boolean axisMoved(Controller controller, int axisIndex, float value) {
			//			if (controller == Xbox360Controllers.getController(0)) {
			//				if (axisIndex == Xbox360Mappings.AXIS_LX) {
			//					if (value <= -0.75f) {
			//						pressLeft();
			//					} else if (value >= 0.75f) {
			//						pressRight();
			//					}
			//				} else if (axisIndex == Xbox360Mappings.AXIS_LY) {
			//					if (value <= -0.75f) {
			//						pressUp();
			//					} else if (value >= 0.75f) {
			//						pressDown();
			//					}
			//				}
			//			}
			return false;
		}

		@Override
		public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
			if (controller == Xbox360Controllers.getController(0)
					&& povIndex == Xbox360Mappings.POV) {
				if (value == PovDirection.north) {
					pressUp();
				} else if (value == PovDirection.south) {
					pressDown();
				} else if (value == PovDirection.east) {
					pressRight();
				} else if (value == PovDirection.west) {
					pressLeft();
				}
			}
			return false;
		}
	};

	public MenuTree(Main m, float x, float y, float indent) {
		main = m;
		this.x = x;
		this.y = y;
		this.indent = indent;
	}

	public void render(SpriteBatch batch, BitmapFont font, float alpha) {
		float offsetX = 0;
		float offsetY = 0;

		renderSublevel(batch, font, offsetX, offsetY, elements, selected, true, alpha);
	}
	
	public void renderUpdate(){
		if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			pressDown();
		} else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			pressUp();
		} else if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			pressLeft();
		} else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			pressRight();
		} else if (Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			pressEnter();
		} else if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE)
				|| Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			pressBack();
		}
	}

	protected void pressEnter() {
		if (elements.get(selected).isEnabled()) elements.get(selected).handleEnter();
	}

	protected void pressBack() {
		pressLeft();
	}

	protected void pressUp() {
		increaseSelected(-1);
	}

	protected void pressDown() {
		increaseSelected(1);
	}

	protected void pressLeft() {
		if (elements.get(selected).onNextSublevel) {
			// tell to move left
			elements.get(selected).moveSublevel(false);
		}
	}

	protected void pressRight() {
		if (elements.get(selected).sublevel.size > 0) {
			elements.get(selected).moveSublevel(true);
		}
	}

	public void onScreenShow() {
		// check to init controllers
		if (Xbox360Controllers.hasAny()) {
			Controllers.addListener(controllerAdapter);
		}
	}

	public void onScreenHide() {
		// check to init controllers
		if (Xbox360Controllers.hasAny()) {
			Controllers.removeListener(controllerAdapter);
		}
	}

	private float renderSublevel(SpriteBatch batch, BitmapFont font, float offsetX, float offsetY,
			Array<MenuElement> level, int selected, boolean isThisGroupEvenSelected, float alpha) {
		boolean shouldBeGreyedOut = false;

		// grey out if group not selected
		if (!isThisGroupEvenSelected) {
			shouldBeGreyedOut = true;
		}

		// grey out if not already and any members of group are in their sublevel
		if (!shouldBeGreyedOut) {
			for (MenuElement m : level) {
				if (m.onNextSublevel) {
					shouldBeGreyedOut = true;
					break;
				}
			}
		}

		for (int i = 0; i < level.size; i++) {
			// draw text
			font.setColor(1, 1, 1, alpha);

			if (shouldBeGreyedOut || !level.get(i).isEnabled()) {
				font.setColor(0.25f, 0.25f, 0.25f, alpha);
			} else {
				if (i == selected) {
					font.setColor(0.25f, 0.75f, 1f, alpha);
				}
			}

			font.draw(batch, level.get(i).getRenderText(), x * Gdx.graphics.getWidth() + offsetX, y
					* Gdx.graphics.getHeight() - offsetY);

			if (level.get(i).sublevel.size > 0) {
				main.font.setColor(0.25f, 0.25f, 0.25f, alpha);
				main.font.draw(
						batch,
						"   >",
						x * Gdx.graphics.getWidth() + offsetX
								+ Utils.getWidth(font, level.get(i).getRenderText()), y
								* Gdx.graphics.getHeight() - offsetY);
				main.font.setColor(1, 1, 1, alpha);
			}

			// show the selected arrow if the menu is:
			//    - selected, the group selected, and not on the next sublevel
			if (i == selected && isThisGroupEvenSelected && !level.get(i).onNextSublevel) {
				font.draw(batch, "> ",
						x * Gdx.graphics.getWidth() + offsetX - Utils.getWidth(font, "> "), y
								* Gdx.graphics.getHeight() - offsetY);
			}

			// increase Y offset
			offsetY += font.getCapHeight() * 2.5f;

			// find sublevels, recurse through them and render
			if (level.get(i).sublevel.size > 0 && i == selected && isThisGroupEvenSelected) {
				offsetY = renderSublevel(batch, font, offsetX + indent, offsetY,
						level.get(i).sublevel, level.get(i).getSelected(),
						level.get(i).onNextSublevel, alpha);
			}
		}

		return offsetY;
	}

	public MenuTree addElement(MenuElement me) {
		if (me == null) throw new IllegalArgumentException("MenuElement cannot be null!");

		elements.add(me);
		recomputeTotalElements();

		return this;
	}

	public int recomputeTotalElements() {
		// reset total elements number
		totalElementsCount = 0;

		// reset recomputation queue
		resetRecomputationQueue();

		// traverse recomputation queue, adding new sublevels as we go
		int index = 0;
		MenuElement element = null;
		while (recomputationQueue.size > 0 && index < recomputationQueue.size) {
			element = recomputationQueue.get(index);
			// add this element
			totalElementsCount++;

			index++;

			// traverse this element's sublevel
			if (element.sublevel.size <= 0) continue;
			for (MenuElement m : element.sublevel) {
				// add the sublevel elements into the recomputation
				recomputationQueue.add(m);
			}
		}

		return getAmountOfTotalElements();
	}

	/**
	 * Clears and refills the recomputation queue with the topmost elements
	 */
	private void resetRecomputationQueue() {
		recomputationQueue.clear();
		for (MenuElement m : elements) {
			recomputationQueue.add(m);
		}
	}

	public int getAmountOfTotalElements() {
		return totalElementsCount;
	}

	public int getSelected() {
		return selected;
	}

	public void increaseSelected(int amt) {
		if (!elements.get(selected).onNextSublevel) {
			selected = MathUtils.clamp(selected + amt, 0, elements.size - 1);
		} else {
			elements.get(selected).increaseSelected(amt);
		}
	}

}
