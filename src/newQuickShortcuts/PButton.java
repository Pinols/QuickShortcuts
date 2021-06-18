package newQuickShortcuts;

import javax.swing.JButton;

public class PButton extends JButton{
private static final long serialVersionUID = 1L;
	
	public String link;
	public PButton(String name,String link) {
		super(name);
		this.link=link;
	}
	public String getLink() {
		return this.link;
	}
}