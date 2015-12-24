package ionium.conversation;

public class Conversation {
	
	public DialogueLine[] lines;
	public String gotoNext = null;
	public Choice[] choices = null;
	
	public Conversation(DialogueLine[] lines, String next){
		this(lines, next, null);
	}
	
	public Conversation(DialogueLine[] lines, String next, Choice[] choices){
		this.lines = lines;
		this.gotoNext = next;
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
