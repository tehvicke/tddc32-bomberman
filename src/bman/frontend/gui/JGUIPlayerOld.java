//package bman.frontend.gui;
//
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.awt.event.KeyEvent;
//
//public class JGUIPlayerOld extends JGUIMapObject {
//
//	/**
//	 * Members
//	 */
//	int dx=0;
//	int dy=0;
//	JGUIMapObject[][] gamemap;
//	/**
//	 * 
//	 * @param frontsp
//	 * @param backsp
//	 * @param leftsp
//	 * @param rightsp
//	 * @param xpos
//	 * @param ypos
//	 */
//	public JGUIPlayerOld(String frontsp, String backsp, String leftsp, String rightsp, int xpos, int ypos,JGUIMapObject[][] gamemap) {
//		super(frontsp,backsp,leftsp,rightsp,xpos,ypos);		
//		this.gamemap = gamemap;
//	}
//	
//	protected void move() {
//		if (dx > 0) {
//			active = left;
//		}
//		else if (dx < 0) {
//			active = right;
//		}
//		else if (dy > 0) {
//			active = front;
//		}
//		else if (dy < 0) {
//			active = back;
//		}
//		//
//		x_pos += dx;
//		y_pos += dy;
//	}
//	
//	protected Image getImage() {
//		return active;
//	}
//	
//	protected int getX() {
//		return x_pos;
//	}
//	protected int getY() {
//		return y_pos;
//	}
//	
//	void keypress(KeyEvent e) {
//		int key = e.getKeyCode();
//		if (key == KeyEvent.VK_LEFT) {
//			dx = -1;
//		} else if (key == KeyEvent.VK_RIGHT) {
//			dx = 1;
//		} else if (key == KeyEvent.VK_UP) {
//			dy = -1;
//		} else if (key == KeyEvent.VK_DOWN) {
//			dy = 1;
//		}
//	}
//	
//	void keyrelease(KeyEvent e) {
//		int key = e.getKeyCode();
//		if (key == KeyEvent.VK_LEFT) {
//			dx = 0;
//		} else if (key == KeyEvent.VK_RIGHT) {
//			dx = 0;
//		} else if (key == KeyEvent.VK_UP) {
//			dy = 0;
//		} else if (key == KeyEvent.VK_DOWN) {
//			dy = 0;
//		}	
//		
//	}
//	
//	
//}
