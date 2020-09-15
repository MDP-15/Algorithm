package mdpsimGUI;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Panel extends JPanel{

	public ArrayList<Line> lines;
	public ArrayList<Circle> circles;
	private static final long serialVersionUID = 7177602708373003430L;

	public Panel() {
		super();
		constructP();
	}
	
	public Panel(LayoutManager lm) {
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
		for (Line line: lines) {
			g2.setColor(line.color);
			g2.setStroke(new BasicStroke(3));
			g2.drawLine(line.start.x(), line.start.y(), line.end.x(), line.end.y());
		}
		for (Circle circle: circles) {
			g2.setColor(circle.color);
			g2.fillOval(circle.position.x(),circle.position.y(), circle.diameter, circle.diameter);
		}
		if (verbose) {
			System.out.println(circles.size() +" circles drawn.");
			System.out.println(lines.size() + " lines drawn.");
		}
		return;
	}
}
