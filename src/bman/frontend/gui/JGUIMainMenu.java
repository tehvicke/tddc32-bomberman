package bman.frontend.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class JGUIMainMenu extends JPanel implements ActionListener  {

	/**
	 * ID
	 */
	private static final long serialVersionUID = -4776987440183048444L;
	JLabel banan;
	
	public JGUIMainMenu() {
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		this.setVisible(true);
		banan = new JLabel("BADABANG");
		this.add(banan);
		Timer t = new Timer();
		
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		banan = new JLabel("badabong");
		repaint();
		
	}

}
