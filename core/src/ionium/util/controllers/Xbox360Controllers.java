package ionium.util.controllers;

import ionium.templates.Main;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;


public class Xbox360Controllers {

	private static Xbox360Controllers instance;

	private Xbox360Controllers() {
	}

	public static Xbox360Controllers instance() {
		if (instance == null) {
			instance = new Xbox360Controllers();
			instance.loadResources();
		}
		return instance;
	}

	private Array<Controller> controllers = new Array<>(4);
	private boolean initialized = false;
	
	private void loadResources() {

	}
	
	/**
	 * Must be called on the rendering thread
	 */
	public void findControllers(){
		if(initialized){
			Main.logger.warn("Warning: something attempted to re-search for controllers!");
			return;
		}
		
		controllers.clear();
		
		Main.logger.info("Searching for controllers...");
		
		if(Controllers.getControllers().size > 0){
			Array<Controller> total = Controllers.getControllers();
			
			for(int i = 0; i < total.size; i++){
				if(total.get(i).getName().contains("XBOX") && total.get(i).getName().contains("360")){
					controllers.add(total.get(i));
				}
			}
			
			Main.logger.info("Found " + controllers.size + " Xbox 360 controllers");
		}else{
			Main.logger.info("No controllers found!");
		}
		
		initialized = true;
	}
	
	public static Controller getController(int i){
		return instance().controllers.get(i);
	}
	
	public static boolean hasAny(){
		return instance().controllers.size > 0;
	}
	
	public static boolean initialized(){
		return instance().initialized;
	}
	
}
