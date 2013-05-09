package bman.frontend.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class JGUISplashScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3494347439059759205L;
	Image logo;
	public JGUISplashScreen() {
		this.setBackground(Color.white);
		logo = Toolkit.getDefaultToolkit().getImage("./sprites/splash.png");
		this.setVisible(true);
		
	}
	

	public void paint(Graphics g) {
		
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(logo,0,0,this);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.paint(g);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

}
