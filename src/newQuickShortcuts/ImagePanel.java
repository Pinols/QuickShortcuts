package newQuickShortcuts;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 6390476351582765721L;
	private Image img=null;

    ImagePanel(Image image) {
    	this.img=image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}