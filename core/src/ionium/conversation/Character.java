package ionium.conversation;


public class Character {

	/**
	 * the localized name prefix
	 * <br>
	 * appended to the beginning of the character's name in the locale files
	 */
	public static String LocalizedNamePrefix = "character.name.";
	
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
