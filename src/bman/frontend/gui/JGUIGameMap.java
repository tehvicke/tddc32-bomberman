package bman.frontend.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class JGUIGameMap extends JPanel implements ActionListener {

	/**
	 * VARIABLES
	 */
	private static final long serialVersionUID = -5735185698246996895L;

	/**
	 * Contents
	 */
	JGUIPlayer player;
	JGUIMapObject[][] gameMap;
	static int gridsize = 15;
	protected int gridXSize = JGUIScreen.w_width/gridsize;
	protected int gridYSize = JGUIScreen.w_heigth/gridsize;

	public JGUIGameMap() {
		setSize(JGUIScreen.w_width, JGUIScreen.w_heigth);
		setVisible(true);
		this.setBackground(Color.green);
		String sprite = "./sprites/white_front.png";
		player = new JGUIPlayer(sprite,"./sprites/white_back.png","./sprites/superman.png",sprite,10,10,gameMap);
		addKeyListener(new KAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		Timer timer = new Timer(6, this);
		timer.start();
		gameMap = new JGUIMapObject[15][15];
		//Test layout
		String block = "./sprites/solid_block.png"; 
		for (int i = 0; i < 15; i++) {
			gameMap[0][i] = new JGUIMapObject(block,i*30,0);
			gameMap[14][i]= new JGUIMapObject(block,i*30,14*30);
		}
		for (int i = 1; i < 15; i++) {
			gameMap[i][0] = new JGUIMapObject(block,0,i*30);
			gameMap[i][14] = new JGUIMapObject(block,14*30,i*30);
		}
		gameMap[5][5] = new JGUIMapObject("./sprites/superman.png",5*30,5*30);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
	
		for (int i = 0; i <= JGUIScreen.w_heigth; i +=30) {
			g2d.setColor(Color.black);
			g2d.drawLine(0, i, JGUIScreen.w_width,i );
		}
		for (int i = 0; i <= JGUIScreen.w_width; i +=30) {
			g2d.drawLine(i, 0,i,JGUIScreen.w_heigth);
		}
		
		g2d.drawImage(player.getImage(),player.getX(),player.getY(),this);
		for (int i = 0; i < gridsize; i++) {
			for (int j = 0; j < gridsize ; j++) {
				try {
				g2d.drawImage(gameMap[i][j].getImage(),gameMap[i][j].getX(),gameMap[i][j].getY(),this);
				} catch (Exception e) {
					
				}
			}
		}
		
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		player.move();
		repaint();

	}

	private class KAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			player.keypress(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			player.keyrelease(e);
		}
	}

}
