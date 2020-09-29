package mdpsimGUI;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class Robot2DPanel extends JPanel{
	
	static final int cols = 15;
	static final int rows = 20;
	
	static final int originX = 0;
	static final int originY = 0;
	static final int cellSize = 22;
	
	public ArrayList<ArrayList<Integer>> map;
	
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
		g.setColor(Color.WHITE);
		super.paintComponent(g);
		//DRAW GRID LINES
		for(i=0;i<rows+1;i++) {
			g.drawLine(originX, originY + i * cellSize, originX+cols*cellSize , originY + i * cellSize);
		}
				
		for(j=0;j<cols+1;j++) {
			g.drawLine(originX+ j * cellSize, originY, originX+ j * cellSize , originY+rows*cellSize);
		}
		//DRAW MAP
		int x_size = map.size();
		int y_size;
		try {
			y_size = map.get(0).size();
		} catch(Exception e) {
			y_size = 0;
		}
		for (int a = 0; a < x_size; a++) {
			for (int b = 0; b < y_size; b++){
				if(map.get(a).get(b) == 0) {
					g.setColor(Color.white);
				} else if (map.get(a).get(b) == 1){
					g.setColor(Color.green);
				} else if (map.get(a).get(b) == 2){
					g.setColor(Color.blue);
				} else if (map.get(a).get(b) == 3){
					g.setColor(Color.yellow);
				}
				g.fillRect(a*cellSize,  b*cellSize, cellSize, cellSize);
			}
		}
	}
}
