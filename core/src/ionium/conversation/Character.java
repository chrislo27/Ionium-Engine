package ionium.conversation;


public class Character {

	public String name;
	public String voice;
	
	public Character(String name, String soundAsset){
		this.name = name;
		this.voice = soundAsset;
	}
	
	@Override
	/**
	 * Returns true if the name matches
	 */
	public boolean equals(Object obj) {
		if(obj instanceof Character){
			if(((Character) obj).name.equals(name)) return true;
		}
		
		return super.equals(obj);
	}
	
}
