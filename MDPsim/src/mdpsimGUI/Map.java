package mdpsimGUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Map extends JPanel{
	
	//Circle2D circle = new Circle2D();
	//Line2D line = new Line2D();

	private final static int SPACING = 5;
	private final static int GAP = 3;
	private final static int THICKNESS = 2;
	
	public static void paintMap(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(4, 4, 450, 600);
		g.setColor(Color.white);
		for(int i = 0; i<15; i++) {
			for(int j =0; j<20; j++) {
				g.fillRect(SPACING+i*30,SPACING+j*30, 30-GAP, 30-GAP);

			}
		}

		//END ZONE
		g.setColor(Color.gray);
		for(int x=12; x<15; x++) {
			for(int y=0; y<3;y++) {
				g.fillRect(SPACING+x*30,SPACING+y*30, 30-GAP, 30-GAP);
			}
		}

		//START ZONE
		g.setColor(Color.gray);
		for(int x=0; x<3; x++) {
			for(int y=17; y<20;y++) {
				g.fillRect(SPACING+x*30,SPACING+y*30, 30-GAP, 30-GAP);
			}
		}

		//STATIC OBSTACLES
		g.setColor(Color.black);
		for(int x=5; x<6; x++) {
			for(int y=5; y<10;y++) {
				g.fillRect(SPACING+x*30,SPACING+y*30, 30-GAP, 30-GAP);
			}
		}

		g.setColor(Color.black);
		for(int x=6; x<9; x++) {
			for(int y=2; y<3;y++) {
				g.fillRect(SPACING+x*30,SPACING+y*30, 30-GAP, 30-GAP);
			}
		}

		g.setColor(Color.black);
		for(int x=10; x<13; x++) {
			for(int y=8; y<9;y++) {
				g.fillRect(SPACING+x*30,SPACING+y*30, 30-GAP, 30-GAP);
			}
		}
		g.setColor(Color.black);
		for(int x=9; x<12; x++) {
			for(int y=9; y<10;y++) {
				g.fillRect(SPACING+x*30,SPACING+y*30, 30-GAP, 30-GAP);
			}
		}
	}

	//BASIC MAP LAYOUT
	public void paintComponent(Graphics g) {
		paintMap(g);
	}
}
