package mdpsimGUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

import mdpsimRobot.Robot;

public class Engine2DPanel extends JPanel{

	public ArrayList<Line> lines;
	public ArrayList<Circle> circles;
	private static final long serialVersionUID = 7177602708373003430L;

	public Engine2DPanel() {
		super();
		constructP();
	}
	
	public Engine2DPanel(LayoutManager lm) {
		super(lm);
		constructP();
	}
	
	public void constructP() {
		lines = new ArrayList<Line>();
		circles = new ArrayList<Circle>();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		boolean verbose = false;
		super.paintComponent(g);
		
		//PAINT WAYPOINT
//		g.setColor(Color.orange);
//		g.fillRect((Robot.way_x*22), (Robot.way_y*22), 22, 22);
		
		//PAINT GRID CIRCLES
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.lightGray);
		for(int a = 1; a < 15; a++) {
			for (int b = 1; b < 20; b++) {
				VecInt pos = new VecInt((a*this.getWidth()/15)-2, (b*this.getHeight()/20)-2);
				g2.fillOval(pos.x(), pos.y(), 5,5);
			}
		}
		for (int a = 0; a < circles.size(); a++) {
			g2.setColor(circles.get(a).color);
			if (circles.get(a).fill) {
				g2.fillOval(circles.get(a).position.x(),circles.get(a).position.y(), circles.get(a).diameter, circles.get(a).diameter);
			} else {
				g2.drawOval(circles.get(a).position.x(),circles.get(a).position.y(), circles.get(a).diameter, circles.get(a).diameter);
			}
		}
		for (int b = 0; b < lines.size(); b++) {
			g2.setColor(lines.get(b).color);
			g2.setStroke(new BasicStroke(lines.get(b).thickness));
			g2.drawLine(lines.get(b).start.x(), lines.get(b).start.y(), lines.get(b).end.x(), lines.get(b).end.y());
		}
		if (verbose) {
			System.out.println(circles.size() +" circles drawn.");
			System.out.println(lines.size() + " lines drawn.");
		}
		return;
		
	}
}
