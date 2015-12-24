package ionium.conversation;

public class Conversation {
	
	public DialogueLine[] lines;
	
	public Choice[] choices = null;
	
	public Conversation(DialogueLine[] lines){
		this(lines, null);
	}
	
	public Conversation(DialogueLine[] lines, Choice[] choices){
		this.lines = lines;
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
