package mdpsimGUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

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
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.lightGray);
		for(int a = 1; a < 15; a++) {
			for (int b = 1; b < 20; b++) {
				VecInt pos = new VecInt((a*this.getWidth()/15)-2, (b*this.getHeight()/20)-2);
				g2.fillOval(pos.x(), pos.y(), 5,5);
			}
		}
		for (Circle circle: circles) {
			g2.setColor(circle.color);
			if (circle.fill) {
				g2.fillOval(circle.position.x(),circle.position.y(), circle.diameter, circle.diameter);
			} else {
				g2.drawOval(circle.position.x(),circle.position.y(), circle.diameter, circle.diameter);
			}
		}
		for (Line line: lines) {
			g2.setColor(line.color);
			g2.setStroke(new BasicStroke(line.thickness));
			g2.drawLine(line.start.x(), line.start.y(), line.end.x(), line.end.y());
		}
		if (verbose) {
			System.out.println(circles.size() +" circles drawn.");
			System.out.println(lines.size() + " lines drawn.");
		}
		return;
	}
}
