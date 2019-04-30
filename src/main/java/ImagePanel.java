/************************************************************************
 * \brief: imagePanel class                                              *
 *																		*
 * (c) copyright by Jörn Fischer											*
 *                                                                       *																		*
 * @autor: Prof.Dr.Jörn Fischer											*
 * @email: j.fischer@hs-mannheim.de										*
 *                                                                       *
 * @file : ImagePanel.java                                               *
 *************************************************************************/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	
	public final Image img;
	
	public ImagePanel(Image img) {
		this.img = img;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, 0, 0, null);
	}
	
	public void drawPixel(int x, int y, Color color) {
		Graphics graphics = img.getGraphics();
		graphics.setColor(color);
		graphics.fillRect(x, y, 1, 1);
	}
	
	public void fillRect(int x, int y, int width, int height, Color color) {
		Graphics graphics = img.getGraphics();
		graphics.setColor(color);
		graphics.fillRect(x, y, width, height);
	}
}
