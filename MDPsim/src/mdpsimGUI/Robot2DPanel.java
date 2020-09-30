package mdpsimGUI;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class Robot2DPanel extends JPanel{
	public Robot2DPanel() {
		super();
	}
	
	public Robot2DPanel(LayoutManager lm) {
		super(lm);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
<<<<<<< Updated upstream
=======
		
<<<<<<< Updated upstream
=======
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
				} else if (map.get(a).get(b) == 1){ //OBSTACLE
					g.setColor(Color.green);
				} else if (map.get(a).get(b) == 2){ //EXPLORED
					g.setColor(Color.blue);
				} else if (map.get(a).get(b) == 3){ //ROBOT POSITION
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
>>>>>>> Stashed changes
		//DRAW GRID LINES
		for(i=0;i<rows+1;i++) {
			g.drawLine(originX, originY + i * cellSize, originX+cols*cellSize , originY + i * cellSize);
		}
				
		for(j=0;j<cols+1;j++) {
			g.drawLine(originX+ j * cellSize, originY, originX+ j * cellSize , originY+rows*cellSize);
		}
>>>>>>> Stashed changes
	}
}
