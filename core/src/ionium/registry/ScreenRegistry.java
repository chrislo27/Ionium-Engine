package ionium.registry;

import ionium.screen.Updateable;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


public class ScreenRegistry implements Disposable{

	private static ScreenRegistry instance;

	private ScreenRegistry() {
	}

	public static ScreenRegistry instance() {
		if (instance == null) {
			instance = new ScreenRegistry();
			instance.loadResources();
		}
		return instance;
	}

	private HashMap<String, Updateable> updateables = new HashMap<>(16);
	private Array<Updateable> all = new Array<>(16);
	
	private void loadResources() {

	}

	public ScreenRegistry add(String id, Updateable up){
		updateables.put(id, up);
		all.add(up);
		
		return this;
	}
	
	public static Updateable get(String id){
		return instance().updateables.get(id);
	}
	
	public static <T extends Updateable> T get(String id, Class<T> cls){
		return cls.cast(get(id));
	}
	
	@Override
	public void dispose(){
		for(Updateable entry : updateables.values()){
			entry.dispose();
		}
	}
	
	public Array<Updateable> getAll(){
		return all;
	}
	
}
