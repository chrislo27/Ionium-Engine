package ionium.conversation;

public class DialogueLine {

	public Character character;
	public String line;
	public String gotoNext = null;

	public Choice[] choices = null;

	public DialogueLine(Character c, String line) {
		this(c, line, null);
	}

	public DialogueLine(Character c, String line, String gotoNext) {
		this(c, line, gotoNext, null);
	}
	
	public DialogueLine(Character c, String line, String gotoNext, Choice[] choices){
		this.character = c;
		this.line = line;
		this.gotoNext = gotoNext;
		
		this.choices = choices;
	}

	public static class Choice {

		public String question;
		public String gotoNext = null;

		public Choice(String question, String gotoNext) {
			this.question = question;
			this.gotoNext = gotoNext;
		}
	}

}
