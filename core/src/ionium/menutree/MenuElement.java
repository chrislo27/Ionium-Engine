package ionium.menutree;

import ionium.templates.Main;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public abstract class MenuElement {

	public Array<MenuElement> sublevel = new Array<>();

	protected MenuTree menuTree;

	private int selectedInSub = 0;
	public boolean onNextSublevel = false;

	public MenuElement(MenuTree tree) {
		this.menuTree = tree;
	}

	public abstract String getRenderText();
	
	public void handleEnter(){
		if(sublevel.size > 0){
			if(!onNextSublevel){
				onNextSublevel = true;
			}else{
				sublevel.get(selectedInSub).handleEnter();
			}
		}
	}

	public MenuElement setSublevel(Array<MenuElement> array) {
		sublevel = array;

		return this;
	}

	public int getSelected() {
		return selectedInSub;
	}

	public void increaseSelected(int amt) {
		if (onNextSublevel) {
			// if in the sublevel
			
			// if the selected in the sublevel IS selected
			if(sublevel.get(selectedInSub).onNextSublevel){
				// tell it to move
				sublevel.get(selectedInSub).increaseSelected(amt);
			}else{
				// if not move THIS one's selected
				selectedInSub = MathUtils.clamp(selectedInSub + amt, 0, sublevel.size - 1);
			}
		}
	}

	public boolean moveSublevel(boolean right) {
		boolean old = onNextSublevel;

		// if this is on the next sublevel
		if (onNextSublevel) {
			// move sublevel on the sublevel, if nothing changed move itself
			if(!sublevel.get(selectedInSub).moveSublevel(right)) onNextSublevel = right;
		}else{
			if(sublevel.size > 0) onNextSublevel = right;
		}

		// correction for elements that don't have sublevels
		if (sublevel.size == 0) {
			onNextSublevel = false;
			
			return false;
		}

		return !(old == onNextSublevel);
	}

}
