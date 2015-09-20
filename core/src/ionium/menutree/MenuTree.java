package ionium.menutree;

import ionium.templates.Main;
import ionium.util.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

	public MenuTree(Main m, float x, float y, float indent) {
		main = m;
		this.x = x;
		this.y = y;
		this.indent = indent;
	}

	public void render(SpriteBatch batch, BitmapFont font) {
		float offsetX = 0;
		float offsetY = 0;

		// input
		if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			increaseSelected(1);
		} else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			increaseSelected(-1);
		} else if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			if (elements.get(selected).onNextSublevel) {
				// tell to move left
				elements.get(selected).moveSublevel(false);
			}
		} else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			if (elements.get(selected).sublevel.size > 0) {
				elements.get(selected).moveSublevel(true);
			}
		}

		renderSublevel(batch, font, offsetX, offsetY, elements, selected, true);
	}

	private float renderSublevel(SpriteBatch batch, BitmapFont font, float offsetX, float offsetY,
			Array<MenuElement> level, int selected, boolean isThisGroupEvenSelected) {
		boolean shouldBeGreyedOut = false;
		
		// grey out if group not selected
		if(!isThisGroupEvenSelected){
			shouldBeGreyedOut = true;
		}
		
		// grey out if not already and any members of group are in their sublevel
		if(!shouldBeGreyedOut){
			for(MenuElement m : level){
				if(m.onNextSublevel){
					shouldBeGreyedOut = true;
					break;
				}
			}
		}
		
		for (int i = 0; i < level.size; i++) {
			// draw text
			font.setColor(1, 1, 1, 1);
			
			if(shouldBeGreyedOut){
				font.setColor(0.25f, 0.25f, 0.25f, 1f);
			}else{
				if(i == selected){
					font.setColor(0.25f, 0.75f, 1f, 1f);
				}
			}

			font.draw(batch, level.get(i).getRenderText(), x * Gdx.graphics.getWidth()
					+ offsetX, y * Gdx.graphics.getHeight() - offsetY);
			
			if(level.get(i).sublevel.size > 0){
				main.font.setColor(0.25f, 0.25f, 0.25f, 1f);
				main.font.draw(batch, "   >", x * Gdx.graphics.getWidth()
						+ offsetX + Utils.getWidth(font, level.get(i).getRenderText()), y * Gdx.graphics.getHeight() - offsetY);
				main.font.setColor(1, 1, 1, 1);
			}
			
			// show the selected arrow if the menu is:
			//    - selected, the group selected, and not on the next sublevel
			if(i == selected && isThisGroupEvenSelected && !level.get(i).onNextSublevel){
				font.draw(batch, "> ", x * Gdx.graphics.getWidth()
						+ offsetX - Utils.getWidth(font, "> "), y * Gdx.graphics.getHeight() - offsetY);
			}

			// increase Y offset
			offsetY += font.getCapHeight() * 2.5f;

			// find sublevels, recurse through them and render
			if (level.get(i).sublevel.size > 0 && i == selected && isThisGroupEvenSelected) {
				offsetY = renderSublevel(batch, font, offsetX + indent, offsetY,
						level.get(i).sublevel, level.get(i).getSelected(), level.get(i).onNextSublevel);
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
