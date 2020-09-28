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
