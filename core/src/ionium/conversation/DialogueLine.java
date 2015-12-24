package ionium.conversation;

public class DialogueLine {

	public Character character;
	public String line;
	public String gotoNext = null;

	public DialogueLine(Character c, String line) {
		this(c, line, null);
	}

	public DialogueLine(Character c, String line, String gotoNext) {
		this.character = c;
		this.line = line;
		this.gotoNext = gotoNext;
	}

}
