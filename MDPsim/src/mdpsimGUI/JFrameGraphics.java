/*package mdpsimGUI;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFrameGraphics extends JPanel{
	public void paint(Graphics g){
		g.drawLine(10, 10, 200, 300);
		return;
	}
	
	public static void GUI(String[] args){
		JFrame frame= new JFrame("MDPsim");	
		frame.getContentPane().add(new JFrameGraphics());
		frame.setSize(1024, 768);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
		return;
	}
	
	public static void update(String[] args){
		JFrame frame= new JFrame("MDPsim");	
		frame.getContentPane().add(new JFrameGraphics());
		frame.setSize(1024, 768);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
		return;
	}
	
	public static int resizeX(double x) {
		return (int)((x/200*998)+15);
	}
	
	public static int resizeY(double y) {
		return (int)((y/200*738)+15);
	}
}*/
package mdpsimGUI;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFrameGraphics extends JFrame{
	
	int spacing = 5;
	int gap = 3;
	double thickness = 2;
	
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
		}
	}

}
