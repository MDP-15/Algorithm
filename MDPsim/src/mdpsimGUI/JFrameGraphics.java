package mdpsimGUI;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFrameGraphics extends JFrame{
	
	int spacing = 5;
	int gap = 3;
	double thickness = 2;
	double x_cor, y_cor;
	
	
	
	public JFrameGraphics() {
		this.setTitle("Map Descriptor");
		this.setSize(470, 640);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		Map map = new Map();
		this.setContentPane(map);
		
		
	}
	
	public class Map extends JPanel {
		
		//BASIC MAP LAYOUT 
		public void paintComponent(Graphics g) {

			g.setColor(Color.black);
			g.drawRect(4, 4, 450, 600);
			g.setColor(Color.white);
			for(int i = 0; i<15; i++) {
				for(int j =0; j<20; j++) {
					g.fillRect(spacing+i*30,spacing+j*30, 30-gap, 30-gap);
					
				}
			}
			
			//END ZONE 
			g.setColor(Color.gray);
			for(int x=12; x<15; x++) {
				for(int y=0; y<3;y++) {
					g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
				}
			}
			
			//START ZONE
			g.setColor(Color.gray);
			for(int x=0; x<3; x++) {
				for(int y=17; y<20;y++) {
					g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
				}
			}
			
			//STATIC OBSTACLES
			g.setColor(Color.black);
			for(int x=5; x<6; x++) {
				for(int y=5; y<10;y++) {
					g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
				}
			}
			
			g.setColor(Color.black);
			for(int x=6; x<9; x++) {
				for(int y=2; y<3;y++) {
					g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
				}
			}
			
			g.setColor(Color.black);
			for(int x=10; x<13; x++) {
				for(int y=8; y<9;y++) {
					g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
				}
			}
			g.setColor(Color.black);
			for(int x=9; x<12; x++) {
				for(int y=9; y<10;y++) {
					g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
				}
			}
			
			//MAYBE THIS?
			
			int[][] arr = new int[15][20];
			arr[0][0] = 1;
			arr[0][1] = 1;
			arr[1][0] = 2;
			arr[1][1] = 2;
			for(int x=0; x<15; x++) {
				for(int y=0; y<20;y++) {
					if(arr[x][y]==1)
					{
						g.setColor(Color.black);
						g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
					}else if(arr[x][y]==2)
					{
						g.setColor(Color.red);
						g.fillRect(spacing+x*30,spacing+y*30, 30-gap, 30-gap);
					}
				}
			}
			
			//CIRCLE
			Graphics2D gg = (Graphics2D) g;
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g.setColor(Color.green);
			Ellipse2D.Double circle = new Ellipse2D.Double(x_cor, y_cor, 200, 200);
			//g.drawOval(x_cor, y_cor, 100, 100);
		}
		
		public void Circle() {
			
			Scanner input_x = new Scanner(System.in);
			x_cor = input_x.nextDouble();
			Scanner input_y = new Scanner(System.in);
			y_cor = input_y.nextDouble();
			
		}
		
	}
	

}
