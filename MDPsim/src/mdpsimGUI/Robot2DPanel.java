package mdpsimGUI;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import mdpsimRobot.Robot;

public class Robot2DPanel extends JPanel{
	
	static final int cols = 15;
	static final int rows = 20;
	
	static final int originX = 0;
	static final int originY = 0;
	static final int cellSize = 22;
	
	public ArrayList<ArrayList<Integer>> map;
	public ArrayList<Circle> circles;
	
	int i,j;
	
	
	public Robot2DPanel() {
		super();
	}
	
	public Robot2DPanel(LayoutManager lm) {
		super(lm);
		this.map = new ArrayList<ArrayList<Integer>>(0);
		this.map.add(new ArrayList<Integer>(0));
	}
	
	public Robot2DPanel(LayoutManager lm, ArrayList<ArrayList<Integer>> map) {
		super(lm);
		this.map = map;
	}
	
	public void setMap(ArrayList<ArrayList<Integer>> map) {
		this.map = map;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//DRAW MAP
		int x_size = map.size();
		int y_size;
		
		try {
			y_size = map.get(0).size();
		} catch(Exception e) {
			y_size = 0;
		}
		
		for(int a=20;a>16;a--) {
			for(int b=0;b<3;b++) {
				g.setColor(Color.magenta);
				g.fillRect(b*cellSize,  a*cellSize, cellSize, cellSize);
			}
			
		}
		for (int a = 0; a < x_size; a++) {
			for (int b = 0; b < y_size; b++){
				if(map.get(a).get(b) == 0) {
					g.setColor(Color.gray);
				} else if (map.get(a).get(b) == 1){
					g.setColor(Color.green);
				} else if (map.get(a).get(b) == 2){
					g.setColor(Color.blue);
				} else if (map.get(a).get(b) == 3){
					g.setColor(Color.yellow);
				}
				g.fillRect(b*cellSize,  a*cellSize, cellSize, cellSize);
			}
		}
		
		//START ZONE
		for(int a=20;a>16;a--) {
			for(int b=0;b<3;b++) {
				g.setColor(Color.pink);
				g.fillRect(b*cellSize,  a*cellSize, cellSize, cellSize);
			}
			
		}
		
		//END ZONE
		for(int a=0;a<3;a++) {
			for(int b=15;b>11;b--) {
				g.setColor(Color.pink);
				g.fillRect(b*cellSize,  a*cellSize, cellSize, cellSize);
			}
			
		}
//		
		//DRAW GRID LINES
		g.setColor(Color.WHITE);
		for(i=0;i<rows+1;i++) {
			g.drawLine(originX, originY + i * cellSize, originX+cols*cellSize , originY + i * cellSize);
		}
				
		for(j=0;j<cols+1;j++) {
			g.drawLine(originX+ j * cellSize, originY, originX+ j * cellSize , originY+rows*cellSize);
		}
		
		//PAINT WAYPOINT
		g.setColor(Color.orange);
		g.fillRect((Robot.way_x*cellSize), (Robot.way_y*cellSize), cellSize, cellSize);
				
		
		
		//ROBOT 
		for (int a = 0; a < circles.size(); a++) {
			g2.setColor(circles.get(a).color);
			if (circles.get(a).fill) {
				g2.fillOval(circles.get(a).position.x(),circles.get(a).position.y(), circles.get(a).diameter, circles.get(a).diameter);
			} else {
				g2.drawOval(circles.get(a).position.x(),circles.get(a).position.y(), circles.get(a).diameter, circles.get(a).diameter);
			}
		}

	}
}
