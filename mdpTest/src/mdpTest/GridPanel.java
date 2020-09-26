package mdpTest;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics;

public class GridPanel extends JPanel {


	private static final long serialVersionUID = 1L;
	
	static final int cols = 15;
	static final int rows = 20;
	
	static final int originX = 50;
	static final int originY = 50;
	static final int cellSize = 30;
	int updateX = 50;
	int updateY = 50;
	
	String mapInput = "000000010000000\r\n" + 
			"000000000000000\r\n" + 
			"000000000000000\r\n" + 
			"000100000000000\r\n" + 
			"111100000000000\r\n" + 
			"000000001100011\r\n" + 
			"000000001100000\r\n" + 
			"000000001100000\r\n" + 
			"000000000000000\r\n" + 
			"111100000000000\r\n" + 
			"000100000000000\r\n" + 
			"000000000000000\r\n" + 
			"000000000000000\r\n" + 
			"000000000000001\r\n" + 
			"000000111100001\r\n" + 
			"000000000100001\r\n" + 
			"000000000000000\r\n" + 
			"000000000000000\r\n" + 
			"000000000000000\r\n" + 
			"000100000000000";
	String cur;
	
	int i,j,a,b;
	int column = 0;
	int row = 19;
	
	//JLabel axisLabel = new JLabel("", JLabel.CENTER);
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		//DRAW GRID LINES
		for(i=0;i<rows+1;i++) {
			g.drawLine(originX, originY + i * cellSize, originX+cols*cellSize , originY + i * cellSize);
		}
		
		for(j=0;j<cols+1;j++) {
			g.drawLine(originX+ j * cellSize, originY, originX+ j * cellSize , originY+rows*cellSize);
		}
		
		//FILL OBSTACLE SQUARES -> 1 = OBSTACLE 
		for(a=0;a<mapInput.length();a++) {
			cur = String.valueOf(mapInput.charAt(a));//Reading digit at arg0
			
			if(cur.equals("1")) {
				g.fillRect(updateX+column*cellSize, updateY, cellSize, cellSize);
			}
			
			if(column == 14) { //if end of column
				column = 0; //Reset column to first box
				row--; //Move down one row
				updateY = updateY+cellSize;
			}
			column++;
		}
		
		

	}
	

}

